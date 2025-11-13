package vm.words.ua.words.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


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

}

