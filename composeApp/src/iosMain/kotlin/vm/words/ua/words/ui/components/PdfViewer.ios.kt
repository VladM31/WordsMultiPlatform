package vm.words.ua.words.ui.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.*
import platform.UIKit.*
import platform.Foundation.*
import platform.CoreGraphics.*
import platform.QuartzCore.*
import platform.CoreFoundation.*

@OptIn(ExperimentalForeignApi::class)
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
    onOffsetChange: (Float, Float) -> Unit
) {
    var pdfDocument by remember { mutableStateOf<CGPDFDocumentRef?>(null) }
    var imageView by remember { mutableStateOf<UIImageView?>(null) }

    DisposableEffect(pdfData) {
        val nsData = pdfData.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = pdfData.size.toULong())
        }

        val dataProvider = CGDataProviderCreateWithCFData(nsData as CFDataRef)

        dataProvider?.let {
            val document = CGPDFDocumentCreateWithProvider(it)
            pdfDocument = document

            document?.let { doc ->
                val pageCount = CGPDFDocumentGetNumberOfPages(doc).toInt()
                onPageCountChanged(pageCount)
            }
        }

        onDispose {
            // Cleanup handled by ARC
        }
    }

    LaunchedEffect(pdfDocument, currentPage) {
        pdfDocument?.let { document ->
            try {
                val pageNumber = currentPage + 1 // PDF pages are 1-indexed
                val page = CGPDFDocumentGetPage(document, pageNumber.toULong())

                page?.let { pdfPage ->
                    val pageRect = CGPDFPageGetBoxRect(pdfPage, kCGPDFMediaBox)
                    val width = pageRect.useContents { size.width }
                    val height = pageRect.useContents { size.height }

                    // Create bitmap context
                    UIGraphicsBeginImageContextWithOptions(
                        CGSizeMake(width * 2.0, height * 2.0),
                        false,
                        0.0
                    )

                    val context = UIGraphicsGetCurrentContext()
                    context?.let { ctx ->
                        // White background
                        CGContextSetRGBFillColor(ctx, 1.0, 1.0, 1.0, 1.0)
                        CGContextFillRect(ctx, CGRectMake(0.0, 0.0, width * 2.0, height * 2.0))

                        // Flip coordinate system for PDF rendering
                        CGContextTranslateCTM(ctx, 0.0, height * 2.0)
                        CGContextScaleCTM(ctx, 2.0, -2.0)

                        // Draw PDF page
                        CGContextDrawPDFPage(ctx, pdfPage)

                        val image = UIGraphicsGetImageFromCurrentImageContext()
                        UIGraphicsEndImageContext()

                        imageView?.setImage(image)
                    }
                }
            } catch (e: Exception) {
                onError("Failed to render page: ${e.message}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    onScaleChange(scale * zoom)
                    onOffsetChange(pan.x, pan.y)
                }
            }
    ) {
        UIKitView(
            factory = {
                val view = UIImageView()
                view.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
                imageView = view
                view
            },
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        )
    }
}
