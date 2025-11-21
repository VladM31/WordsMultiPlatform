package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getLabelFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.playlist.domain.models.PlayListCount
import vm.words.ua.playlist.ui.actions.PlayListAction
import vm.words.ua.playlist.ui.vms.PlayListViewModel

/**
 * Dialog component to select an existing playlist and confirm with Create.
 * Displays playlist name and word count. Supports infinite scroll via PlayListViewModel.
 *
 * @param onDismiss called when dialog should close without creating
 * @param onPin called with selected playlist id when user presses Create
 * @param viewModel optional injection, defaults to resolved PlayListViewModel
 */
@Suppress("FrequentlyChangingValue")
@Composable
fun SelectPlayListDialog(
    onDismiss: () -> Unit,
    onPin: (String) -> Unit,
    viewModel: PlayListViewModel = rememberInstance()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    var selectedId by remember { mutableStateOf<String?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Trigger loading more when nearing end
    LaunchedEffect(listState.firstVisibleItemIndex, state.isLoading, state.hasMore) {
        val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        val total = state.playlists.size
        if (!state.isLoading && state.hasMore && total > 0 && lastVisible >= total - 3) {
            viewModel.sent(PlayListAction.LoadMore)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier
                    .widthIn(min = 280.dp, max = 420.dp)
                    .heightIn(min = 300.dp, max = 600.dp)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Playlist",
                    color = AppTheme.PrimaryColor,
                    fontWeight = FontWeight.SemiBold
                )

                if (state.error != null) {
                    Text(
                        text = state.error ?: "Error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (state.playlists.isEmpty() && state.isLoading) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                        return@Box
                    }
                    if (state.playlists.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "No playlists found",
                                color = AppTheme.PrimaryColor,
                                fontSize = getFontSize()
                            )
                        }
                        return@Box
                    }
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.playlists, key = { it.id }) { item ->
                            PlayListItem(item, selectedId) {
                                selectedId = item.id
                            }
                        }
                        if (state.isLoading) {
                            item("loading_more") {
                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }

                validationError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onDismiss) { Text("Dismiss") }
                    Spacer(Modifier.width(12.dp))
                    Button(onClick = {
                        val id = selectedId
                        if (id == null) {
                            validationError = "Please select a playlist"
                        } else {
                            onPin(id)
                            onDismiss()
                        }
                    }, enabled = state.playlists.isNotEmpty()) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayListItem(
    item: PlayListCount,
    selectedId: String?,
    onClick: () -> Unit = {}
) {
    val selected = item.id == selectedId

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (selected) AppTheme.PrimaryColor.copy(alpha = 0.15f)
                else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) AppTheme.PrimaryColor.copy(alpha = 0.35f)
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = AppTheme.PrimaryColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                fontSize = getLabelFontSize()
            )
            Text(
                text = "${item.count} words",
                color = AppTheme.PrimaryColor,
                fontSize = getLabelFontSize() * 0.85f
            )
        }
    }
}
