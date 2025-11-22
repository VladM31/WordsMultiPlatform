package vm.words.ua.words.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
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
    val baseUrl = remember { pdfData.decodeToString() }
    val renderClient = rememberInstance<RenderedPdfClient>()
    val fullWidth = appWidthDp()

    var imageContent by remember(currentPage) { mutableStateOf<ByteArray?>(null) }
    var isLoading by remember(currentPage) { mutableStateOf(true) }

    // Убрал remember - теперь width пересчитывается при каждом изменении scale
    val width = fullWidth * 0.95f * scale

    LaunchedEffect(currentPage) {
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
            .fillMaxSize()
            .pointerInput(Unit) {
                // Примитивный зум: даблклик – увеличить, лонгтап – уменьшить
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
            Spacer(modifier = Modifier.height(8.dp))
            return@Box
        }

        imageContent?.let {
            ImageFromBytes(
                imageBytes = it,
                contentScale = ContentScale.Fit,
                width = width,
                height = Dp.Unspecified,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}