package vm.words.ua.words.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vm.words.ua.core.net.client.RenderedPdfClient
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.ImageFromBytes
import vm.words.ua.core.utils.appWidthDp
import vm.words.ua.di.rememberInstance

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
    val baseUrl = remember(pdfData) { pdfData.decodeToString() }
    val renderClient = rememberInstance<RenderedPdfClient>()

    val appWith = appWidthDp()
    val pageBaseWidth = remember { appWith * 0.95f }

    var imageContent by remember(currentPage, baseUrl) { mutableStateOf<ByteArray?>(null) }
    var isLoading by remember(currentPage, baseUrl) { mutableStateOf(true) }

    LaunchedEffect(currentPage, baseUrl) {
        isLoading = true
        try {
            val result = withContext(Dispatchers.Default) {
                renderClient.renderPage(baseUrl, currentPage)
            }
            imageContent = result.content.bytes
            onPageCountChanged(result.totalPages)
        } catch (t: Throwable) {
            val msg = t.message ?: "Unknown pdf render error"
            onError(msg)
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = modifier
            .clipToBounds()          // чтобы страница не вылазила
            .pointerInput(scale) {
                // жесты зума
                detectTapGestures(
                    onDoubleTap = {
                        val newScale = (scale + 0.25f).coerceAtMost(3f)
                        onScaleChange(newScale)
                    },
                    onLongPress = {
                        val newScale = (scale - 0.25f).coerceAtLeast(0.5f)
                        onScaleChange(newScale)
                    }
                )
            }
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = AppTheme.PrimaryGreen,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            imageContent?.let { bytes ->
                // ВАЖНО: масштаб задаём через ширину, а не graphicsLayer
                val widthWithScale = (pageBaseWidth * scale).coerceAtLeast(100.dp)

                ImageFromBytes(
                    imageBytes = bytes,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(widthWithScale)
                )
            }
        }
    }
}
