package vm.words.ua.words.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream

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
    var image by remember { mutableStateOf<BufferedImage?>(null) }
    var document by remember { mutableStateOf<PDDocument?>(null) }
    var renderer by remember { mutableStateOf<PDFRenderer?>(null) }
    val scope = rememberCoroutineScope()

    DisposableEffect(pdfData) {
        scope.launch {
            try {
                val doc = withContext(Dispatchers.IO) {
                    PDDocument.load(ByteArrayInputStream(pdfData))
                }
                document = doc
                renderer = PDFRenderer(doc)
                onPageCountChanged(doc.numberOfPages)
            } catch (e: Exception) {
                onError("Failed to load PDF: ${e.message}")
            }
        }

        onDispose {
            try {
                document?.close()
            } catch (_: Exception) {
                // Ignore close errors
            }
        }
    }

    LaunchedEffect(renderer, currentPage) {
        val pdfRenderer = renderer
        if (pdfRenderer != null) {
            try {
                val pageImage = withContext(Dispatchers.IO) {
                    // Render at 2x resolution for better quality
                    pdfRenderer.renderImageWithDPI(currentPage, 144f)
                }
                image = pageImage
            } catch (e: Exception) {
                onError("Failed to render page: ${e.message}")
            }
        }
    }

    image?.let { img ->
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
            Image(
                bitmap = img.toComposeImageBitmap(),
                contentDescription = "PDF Page ${currentPage + 1}",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                contentScale = ContentScale.Fit
            )
        }
    }
}

