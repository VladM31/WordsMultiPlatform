package vm.words.ua.words.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    var pageCountInitialized by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Scroll to initial page once total pages known
    LaunchedEffect(totalPages) {
        if (totalPages > 0 && !pageCountInitialized) {
            pageCountInitialized = true
            if (initialPage in 0 until totalPages) listState.scrollToItem(initialPage)
        }
    }

    // Derive currently visible page (closest to viewport center)
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

    // Update current page reactively (separate from composition of individual items)
    LaunchedEffect(visiblePageIndex) {
        if (visiblePageIndex != currentPage) currentPage = visiblePageIndex
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            totalPages == 0 -> {
                // Load first page only to obtain total page count (hidden)
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                // Invisible composable just to trigger page count callback
                Box(Modifier.size(1.dp)) {
                    PdfContent(
                        pdfData = pdfData,
                        currentPage = 0,
                        scale = 1f,
                        offsetX = 0f,
                        offsetY = 0f,
                        onPageCountChanged = { totalPages = it },
                        onError = onError,
                        onScaleChange = {},
                        onOffsetChange = { _, _ -> },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp)
                    )
                }
            }

            else -> {
                // Continuous vertical scroll of pages
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(totalPages, key = { it }) { pageIndex ->
                        // Removed side-effect assignment to currentPage here
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            // Render each page; pass scale; offsets not per-page in continuous mode
                            PdfContent(
                                pdfData = pdfData,
                                currentPage = pageIndex,
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
                    }
                }
            }
        }

        // Floating compact zoom controls (non-intrusive)
        if (totalPages > 0) {
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
                        IconButton(onClick = { scale = (scale / 1.15f).coerceAtLeast(0.5f) }) {
                            Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
                        }
                        Text(
                            text = "${(scale * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        IconButton(onClick = { scale = (scale * 1.15f).coerceAtMost(3f) }) {
                            Icon(Icons.Default.Add, contentDescription = "Zoom In")
                        }
                        IconButton(onClick = { scale = 1f }) {
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
    }
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
