package vm.words.ua.words.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vm.words.ua.core.net.client.RenderedPdfClient
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
    val baseUrl = remember {
        pdfData.decodeToString()
    }

    var imageContent by remember {
        mutableStateOf(ByteArray(0))
    }
    val renderClient = rememberInstance<RenderedPdfClient>()
    val fullWidth = appWidthDp()
    val width = remember(scale, fullWidth) {
        fullWidth * 0.9f * scale
    }

    LaunchedEffect(currentPage) {
        withContext(Dispatchers.Default) {
            renderClient.renderPage(baseUrl, currentPage).let {
                imageContent = it.content.bytes
                onPageCountChanged(it.totalPages)
            }
        }
    }

    if (imageContent.isEmpty()) {
        return
    }

    ImageFromBytes(
        imageBytes = imageContent,
        contentScale = ContentScale.Fit,
        width = width,
        height = Dp.Unspecified
    )


}
