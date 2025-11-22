package vm.words.ua.words.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.utils.getWidthDeviceFormat

/**
 * Multiplatform PDF Viewer with pagination and zoom support
 *
 * @param pdfData ByteArray containing the PDF file data
 * @param modifier Modifier for the viewer container
 * @param initialPage Initial page to display (0-based)
 * @param onError Callback for error handling
 */
@Composable
fun PdfViewer(
    pdfData: ByteArray,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    onError: (String) -> Unit = {}
) {
    var currentPage by remember { mutableStateOf(initialPage) }
    var totalPages by remember { mutableStateOf(0) }
    var scale by remember { mutableStateOf(1f) }

    val listState = rememberLazyListState()

    LaunchedEffect(totalPages) {
        if (totalPages > 0) {
            if (initialPage in 0 until totalPages) listState.scrollToItem(initialPage)
        }
    }

    val visiblePageIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visible = layoutInfo.visibleItemsInfo
            if (visible.isEmpty()) currentPage else {
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visible.minBy { item ->
                    val itemCenter = item.offset + item.size / 2
                    kotlin.math.abs(itemCenter - viewportCenter)
                }.index
            }
        }
    }

    LaunchedEffect(visiblePageIndex) {
        if (visiblePageIndex != currentPage) currentPage = visiblePageIndex
    }

    Box(modifier = modifier.fillMaxSize()) {
        println(currentPlatform())
        if (currentPlatform() == AppPlatform.IOS) {
            IOSPdfViewer(pdfData, onError)
            return
        }
        // Show initial loader until we know page count. When PdfContent reports pages, totalPages gets updated.
        if (totalPages == 0) {
            EmptyPageViewer(pdfData, onError) { reported ->
                if (reported > 0 && totalPages == 0) {
                    totalPages = reported
                }
            }
            return
        }

        PagesViewer(
            listState, totalPages, pdfData, scale,
            { new -> scale = new.coerceIn(0.5f, 3f) },
            onError,
            onPageCountChanged = { reported ->
                // If for some reason page count changes (unlikely) keep the max
                if (reported > totalPages) totalPages = reported
            }
        )

        ViewMenu(
            totalPages,
            scale,
            currentPage
        ) { scale = it }
    }
}

@Composable
private fun BoxScope.ViewMenu(
    totalPages: Int,
    scale: Float,
    currentPage: Int,
    onScaleChange: (Float) -> Unit,
) {
    // Previous logic mistakenly returned when totalPages > 0, preventing menu display.
    if (totalPages == 0) {
        return
    }
    Column(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { onScaleChange((scale / 1.15f).coerceAtLeast(0.5f)) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
                }
                Text(
                    text = "${(scale * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                IconButton(onClick = { onScaleChange((scale * 1.15f).coerceAtMost(3f)) }) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In")
                }
                IconButton(onClick = { onScaleChange(1f) }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset Zoom")
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Surface(
            shape = MaterialTheme.shapes.small,
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ) {
            Text(
                text = "Page ${currentPage + 1} / $totalPages",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun PagesViewer(
    listState: LazyListState,
    totalPages: Int,
    pdfData: ByteArray,
    scale: Float,
    onScaleChange: (Float) -> Unit,
    onError: (String) -> Unit,
    onPageCountChanged: (Int) -> Unit
) {

    val basePageHeight = 600.dp
    val deviceFormat = getWidthDeviceFormat()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(totalPages, key = { it }) { pageIndex ->
            val resultScale = if (scale > 1 && deviceFormat.isPhone.not()) scale * 1.6f else scale

            val pageHeight = remember(resultScale) {
                (basePageHeight * scale).coerceIn(300.dp, 2400.dp)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pageHeight)
                    .clipToBounds() // не даём изображению вылезти за пределы Box
            ) {
                PdfContent(
                    pdfData = pdfData,
                    currentPage = pageIndex,
                    scale = scale,
                    offsetX = 0f,
                    offsetY = 0f,
                    onPageCountChanged = onPageCountChanged,
                    onError = onError,
                    onScaleChange = onScaleChange,
                    onOffsetChange = { _, _ -> },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun EmptyPageViewer(pdfData: ByteArray, onError: (String) -> Unit, onPageCountChanged: (Int) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
    // Hidden PdfContent to discover page count then update state.
    Box(Modifier.size(1.dp)) {
        PdfContent(
            pdfData = pdfData,
            currentPage = 0,
            scale = 1f,
            offsetX = 0f,
            offsetY = 0f,
            onPageCountChanged = onPageCountChanged,
            onError = onError,
            onScaleChange = {},
            onOffsetChange = { _, _ -> },
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        )
    }
}

@Composable
private fun IOSPdfViewer(pdfData: ByteArray, onError: (String) -> Unit) {
    var scale by remember { mutableStateOf(1f) }

    PdfContent(
        pdfData = pdfData,
        currentPage = 0,
        scale = scale,
        offsetX = 0f,
        offsetY = 0f,
        onPageCountChanged = {},
        onError = onError,
        onScaleChange = { new -> scale = new.coerceIn(0.5f, 3f) },
        onOffsetChange = { _, _ -> },
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
    )
}

/**
 * Platform-specific PDF content rendering
 * This expect function will have different implementations for each platform
 */
@Composable
expect fun PdfContent(
    pdfData: ByteArray,
    currentPage: Int,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    onPageCountChanged: (Int) -> Unit,
    onError: (String) -> Unit,
    onScaleChange: (Float) -> Unit,
    onOffsetChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
)
