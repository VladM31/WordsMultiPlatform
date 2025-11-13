package vm.words.ua.words.ui.components

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
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
    onOffsetChange: (Float, Float) -> Unit,
    modifier: Modifier
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pdfRenderer by remember { mutableStateOf<PdfRenderer?>(null) }
    var fileDescriptor by remember { mutableStateOf<ParcelFileDescriptor?>(null) }
    var tempFile by remember { mutableStateOf<File?>(null) }
    var renderJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    DisposableEffect(pdfData) {
        // Load PDF into temp file and prepare renderer
        val loadJob = scope.launch(Dispatchers.IO) {
            try {
                // Clean previous
                try {
                    pdfRenderer?.close()
                } catch (_: Throwable) {
                }
                try {
                    fileDescriptor?.close()
                } catch (_: Throwable) {
                }
                try {
                    tempFile?.takeIf { it.exists() }?.delete()
                } catch (_: Throwable) {
                }

                val tmp = File.createTempFile("pdf_temp", ".pdf")
                FileOutputStream(tmp).use { it.write(pdfData) }
                tempFile = tmp

                val fd = ParcelFileDescriptor.open(tmp, ParcelFileDescriptor.MODE_READ_ONLY)
                fileDescriptor = fd
                val renderer = PdfRenderer(fd)
                withContext(Dispatchers.Main) {
                    pdfRenderer = renderer
                    onPageCountChanged(renderer.pageCount)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("Failed to load PDF: ${e.message}") }
            }
        }

        onDispose {
            // Defer cleanup until any rendering job completes to avoid 'Current page not closed'
            val renderer = pdfRenderer
            val fd = fileDescriptor
            val tmp = tempFile
            val job = renderJob

            // Clear state to avoid reuse
            pdfRenderer = null
            fileDescriptor = null
            tempFile = null
            bitmap?.recycle()
            bitmap = null

            scope.launch(Dispatchers.IO) {
                try {
                    loadJob.join()
                } catch (_: Throwable) {
                }
                try {
                    job?.cancelAndJoin()
                } catch (_: Throwable) {
                }
                try {
                    renderer?.close()
                } catch (_: Throwable) {
                }
                try {
                    fd?.close()
                } catch (_: Throwable) {
                }
                try {
                    tmp?.takeIf { it.exists() }?.delete()
                } catch (_: Throwable) {
                }
            }
        }
    }

    LaunchedEffect(pdfRenderer, currentPage) {
        val renderer = pdfRenderer ?: return@LaunchedEffect
        // Cancel any previous render to avoid overlapping pages
        renderJob?.cancel()
        renderJob = launch(Dispatchers.IO) {
            try {
                // Open, render, and close the page explicitly
                val page = renderer.openPage(currentPage)
                try {
                    // Recycle previous bitmap before creating a new one
                    bitmap?.recycle()
                    val newBitmap = Bitmap.createBitmap(
                        page.width * 2,
                        page.height * 2,
                        Bitmap.Config.ARGB_8888
                    )
                    page.render(newBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    withContext(Dispatchers.Main) { bitmap = newBitmap }
                } finally {
                    try {
                        page.close()
                    } catch (_: Throwable) {
                    }
                }
            } catch (e: CancellationException) {
                // expected on re-render or dispose
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError("Failed to render page: ${e.message}") }
            }
        }
    }

    bitmap?.let { bmp ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(scale, offsetX, offsetY) {
                    awaitPointerEventScope {
                        while (true) {
                            var event = awaitPointerEvent()
                            if (event.changes.none { it.pressed }) continue
                            // wait until two fingers are down; let single-finger be handled by parent
                            while (event.changes.any { it.pressed } && event.changes.count { it.pressed } < 2) {
                                event = awaitPointerEvent()
                            }
                            if (event.changes.count { it.pressed } < 2) continue

                            var curScale = scale
                            var curOffsetX = offsetX
                            var curOffsetY = offsetY
                            do {
                                val zoomChange =
                                    runCatching { event.calculateZoom() }.getOrDefault(1f)
                                val panChange =
                                    runCatching { event.calculatePan() }.getOrDefault(androidx.compose.ui.geometry.Offset.Zero)
                                val newScale = (curScale * zoomChange).coerceIn(0.5f, 5f)
                                if (newScale != curScale) {
                                    curScale = newScale
                                    onScaleChange(curScale)
                                }
                                if (curScale > 1f) {
                                    curOffsetX += panChange.x
                                    curOffsetY += panChange.y
                                    onOffsetChange(curOffsetX, curOffsetY)
                                }
                                event.changes.forEach { change -> change.consume() }
                                event = awaitPointerEvent()
                            } while (event.changes.any { it.pressed })
                        }
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
