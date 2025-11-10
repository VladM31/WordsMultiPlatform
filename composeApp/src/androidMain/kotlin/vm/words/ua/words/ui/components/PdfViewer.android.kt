package vm.words.ua.words.ui.components

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

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
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfRenderer by remember { mutableStateOf<PdfRenderer?>(null) }
    val scope = rememberCoroutineScope()

    DisposableEffect(pdfData) {
        scope.launch {
            try {
                val renderer = withContext(Dispatchers.IO) {
                    // Create temporary file for PDF
                    val tempFile = File.createTempFile("pdf_temp", ".pdf")
                    FileOutputStream(tempFile).use { output ->
                        output.write(pdfData)
                    }

                    val fileDescriptor = ParcelFileDescriptor.open(
                        tempFile,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )
                    PdfRenderer(fileDescriptor)
                }

                pdfRenderer = renderer
                onPageCountChanged(renderer.pageCount)
            } catch (e: Exception) {
                onError("Failed to load PDF: ${e.message}")
            }
        }

        onDispose {
            pdfRenderer?.close()
            bitmap?.recycle()
        }
    }

    LaunchedEffect(pdfRenderer, currentPage) {
        pdfRenderer?.let { renderer ->
            try {
                withContext(Dispatchers.IO) {
                    if (currentPage < renderer.pageCount) {
                        bitmap?.recycle()

                        renderer.openPage(currentPage).use { page ->
                            val newBitmap = Bitmap.createBitmap(
                                page.width * 2,
                                page.height * 2,
                                Bitmap.Config.ARGB_8888
                            )
                            page.render(
                                newBitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )
                            bitmap = newBitmap
                        }
                    }
                }
            } catch (e: Exception) {
                onError("Failed to render page: ${e.message}")
            }
        }
    }

    bitmap?.let { bmp ->
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
                bitmap = bmp.asImageBitmap(),
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

