package vm.words.ua.words.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.graphics.Color
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.PDFKit.*
import platform.UIKit.*
import platform.CoreGraphics.*
import kotlin.math.abs
import kotlin.ranges.coerceIn

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
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

    var baseScaleFactor by remember { mutableStateOf<Double?>(null) }

    var ignoreExternalScale by remember { mutableStateOf(false) }

    DisposableEffect(pdfData) {
        if (pdfData.isEmpty()) {
            onError("Empty PDF data")
            pdfDocument = null
            onPageCountChanged(0)
            return@DisposableEffect onDispose {}
        }

        val nsData = pdfData.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = pdfData.size.toULong())
        }
        val document = PDFDocument(data = nsData)
        pdfDocument = document
        onPageCountChanged(document.pageCount.toInt())

        baseScaleFactor = null

        pdfViewRef?.document = document

        onDispose {
            pdfDocument = null
            pdfViewRef = null
            baseScaleFactor = null
        }
    }

    LaunchedEffect(pdfDocument, currentPage) {
        val doc = pdfDocument ?: return@LaunchedEffect
        val view = pdfViewRef

        val pageCount = doc.pageCount.toInt()
        if (pageCount == 0) return@LaunchedEffect

        val clampedIndex = currentPage.coerceIn(0, pageCount - 1)
        val page = doc.pageAtIndex(clampedIndex.toULong()) ?: return@LaunchedEffect

        view?.let {
            if (it.currentPage != page) {
                it.goToPage(page)
            }
        }
    }

    LaunchedEffect(scale, pdfViewRef, baseScaleFactor) {
        val view = pdfViewRef ?: return@LaunchedEffect
        val base = baseScaleFactor ?: return@LaunchedEffect
        if (ignoreExternalScale) return@LaunchedEffect

        val target = (base * scale.toDouble())
            .coerceIn(view.minScaleFactor, view.maxScaleFactor)

        if (abs(view.scaleFactor - target) > 0.0001) {
            view.autoScales = false
            view.setScaleFactor(target)
        }
    }

    Box(
        modifier = modifier
            .background(Color.White)
    ) {
        UIKitView(
            factory = {
                val pdfView = PDFView(
                    frame = CGRectMake(0.0, 0.0, 100.0, 100.0)
                ).apply {
                    setTranslatesAutoresizingMaskIntoConstraints(true)
                    backgroundColor = UIColor.whiteColor
                    clipsToBounds = true

                    displayMode = kPDFDisplaySinglePageContinuous
                    displaysAsBook = false
                    displaysPageBreaks = false
                    pageBreakMargins = UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)

                    autoScales = true
                    userInteractionEnabled = true
                    setOpaque(true)
                }

                NSNotificationCenter.defaultCenter.addObserverForName(
                    name = PDFViewScaleChangedNotification,
                    `object` = pdfView,
                    queue = NSOperationQueue.mainQueue
                ) { _ ->
                    val base = baseScaleFactor
                    if (base != null) {
                        val factor = pdfView.scaleFactor / base
                        ignoreExternalScale = true
                        onScaleChange(factor.toFloat())
                        ignoreExternalScale = false
                    }
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

                    if (baseScaleFactor == null) {
                        baseScaleFactor = view.scaleFactor
                        view.autoScales = false
                    }
                }
            },
            onResize = { view, rect ->
                view.setFrame(rect)
                view.setNeedsLayout()
                val base = baseScaleFactor
                if (base != null) {
                    val target = (base * scale.toDouble()).coerceIn(view.minScaleFactor, view.maxScaleFactor)
                    view.scaleFactor = target
                }
            },
            interactive = true,
            modifier = Modifier.fillMaxSize()
        )
    }
}