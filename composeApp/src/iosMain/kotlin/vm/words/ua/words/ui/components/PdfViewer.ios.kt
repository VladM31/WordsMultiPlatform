package vm.words.ua.words.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.graphics.Color
import platform.Foundation.*
import platform.PDFKit.*
import platform.UIKit.*
import kotlinx.cinterop.*
import kotlin.ranges.coerceIn

@OptIn(ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)
@Composable
actual fun PdfContent(
    pdfData: ByteArray,
    currentPage: Int,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    onPageCountChanged: (Int) -> Unit,
    onError: (String) -> Unit,
    onScaleChange: (Float) -> Unit,
    onOffsetChange: (Float, Float) -> Unit,
    modifier: Modifier
) {
    var pdfDocument by remember { mutableStateOf<PDFDocument?>(null) }
    var pdfViewRef by remember { mutableStateOf<PDFView?>(null) }
    var pdfNsData by remember { mutableStateOf<NSData?>(null) }
    var ignoreExternalScale by remember { mutableStateOf(false) }

    // базовый масштаб "fit"
    var baseScaleFactor by remember { mutableStateOf<Double?>(null) }

    // aspect ratio текущей страницы (width / height)
    var pageAspectRatio by remember { mutableStateOf<Float?>(null) }

    DisposableEffect(pdfData) {
        if (pdfData.isEmpty()) {
            onError("Empty PDF data")
            pdfDocument = null
            pdfNsData = null
            onPageCountChanged(0)
            return@DisposableEffect onDispose {}
        }

        val nsData = pdfData.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = pdfData.size.toULong())
        }
        pdfNsData = nsData
        val document = PDFDocument(data = nsData)
        pdfDocument = document
        onPageCountChanged(document.pageCount.toInt())

        // при новом документе сбрасываем базовый масштаб
        baseScaleFactor = null

        pdfViewRef?.setDocument(document)

        onDispose {
            pdfDocument = null
            pdfNsData = null
            pdfViewRef = null
            baseScaleFactor = null
        }
    }

    // переход на нужную страницу
    LaunchedEffect(pdfDocument, currentPage) {
        val doc = pdfDocument ?: return@LaunchedEffect
        val view = pdfViewRef ?: return@LaunchedEffect
        val pageCount = doc.pageCount.toInt()
        if (pageCount == 0) return@LaunchedEffect
        val clampedIndex = currentPage.coerceIn(0, pageCount - 1)
        val targetPage = doc.pageAtIndex(clampedIndex.toULong())
        if (targetPage != null && view.currentPage != targetPage) {
            view.goToPage(targetPage)
        }
    }

    // считаем aspect ratio текущей страницы
    LaunchedEffect(pdfDocument, currentPage) {
        val doc = pdfDocument ?: return@LaunchedEffect
        val pageCount = doc.pageCount.toInt()
        if (pageCount == 0) return@LaunchedEffect
        val clampedIndex = currentPage.coerceIn(0, pageCount - 1)
        val page = doc.pageAtIndex(clampedIndex.toULong()) ?: return@LaunchedEffect

        val rect = page.boundsForBox(kPDFDisplayBoxMediaBox)
        val width = rect.useContents { size.width }
        val height = rect.useContents { size.height }
        if (width > 0 && height > 0) {
            pageAspectRatio = (width / height).toFloat()
        }
    }

    // применяем scale как множитель к baseScaleFactor (1f = fit)
    LaunchedEffect(scale, pdfViewRef, baseScaleFactor) {
        val view = pdfViewRef ?: return@LaunchedEffect
        val base = baseScaleFactor ?: return@LaunchedEffect
        if (ignoreExternalScale) return@LaunchedEffect

        val target = (base * scale.toDouble())
            .coerceIn(view.minScaleFactor, view.maxScaleFactor)

        if (kotlin.math.abs(view.scaleFactor - target) > 0.0001) {
            view.autoScales = false
            view.setScaleFactor(target)
        }
    }

    // если знаем aspect ratio — подстраиваем высоту под страницу
    val boxModifier =
        pageAspectRatio?.let { ratio ->
            modifier
                .background(Color.White)
                .aspectRatio(ratio)
        } ?: modifier
            .fillMaxSize()
            .background(Color.White)

    Box(modifier = boxModifier) {
        UIKitView(
            factory = {
                val pdfView = PDFView()
                pdfView.setFrame(platform.CoreGraphics.CGRectMake(0.0, 0.0, 100.0, 100.0))

                pdfView.apply {
                    setTranslatesAutoresizingMaskIntoConstraints(true)
                    backgroundColor = UIColor.whiteColor
                    clipsToBounds = true

                    // показываем только одну страницу
                    displayMode = kPDFDisplaySinglePage
                    displaysAsBook = false
                    displaysPageBreaks = false

                    autoScales = true
                    userInteractionEnabled = true
                    setOpaque(true)
                }

                // выключаем внутренний scroll
                pdfView.subviews
                    .filterIsInstance<UIScrollView>()
                    .forEach {
                        it.scrollEnabled = false
                        it.bounces = false
                        it.alwaysBounceVertical = false
                        it.alwaysBounceHorizontal = false
                    }

                pdfViewRef = pdfView
                pdfView
            },
            update = { view ->
                val doc = pdfDocument ?: return@UIKitView
                if (view.document != doc) {
                    view.document = doc
                    view.autoScales = true

                    val pageCount = doc.pageCount.toInt()
                    if (pageCount > 0) {
                        val pageIndex = currentPage.coerceIn(0, pageCount - 1)
                        doc.pageAtIndex(pageIndex.toULong())?.let { page ->
                            view.goToPage(page)
                        }
                    }

                    view.setNeedsLayout()
                    view.layoutIfNeeded()
                    view.setNeedsDisplay()

                    // один раз сохраняем масштаб, который дал autoScales ("fit")
                    if (baseScaleFactor == null) {
                        baseScaleFactor = view.scaleFactor
                    }
                } else {
                    val pageCount = doc.pageCount.toInt()
                    if (pageCount > 0) {
                        val pageIndex = currentPage.coerceIn(0, pageCount - 1)
                        val targetPage = doc.pageAtIndex(pageIndex.toULong())
                        if (targetPage != null && view.currentPage != targetPage) {
                            view.goToPage(targetPage)
                        }
                    }
                }
            },
            onResize = { view, rect ->
                view.setFrame(rect)
                view.setNeedsLayout()
            },
            interactive = true,
            modifier = Modifier.fillMaxSize()
        )
    }
}
