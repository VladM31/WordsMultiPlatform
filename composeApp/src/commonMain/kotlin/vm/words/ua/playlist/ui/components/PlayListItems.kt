package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.playlist.ui.states.PlayListState


@Composable
fun ColumnScope.PlayListItems(
    state: PlayListState,
    listState: LazyListState,
    onPlayListClick: (String) -> Unit = {},
    onPin: (String) -> Unit = {},
    onUnPin: (String) -> Unit = {}
) {
    var pinnedExpanded by rememberSaveable { mutableStateOf(true) }

    val isLoading = state.pinnedPlayList.isLoading || state.otherPlayList.isLoading
    val isEmpty = state.pinnedPlayList.content.isEmpty() && state.otherPlayList.content.isEmpty()

    if (isEmpty && !isLoading) {
        Text(
            text = "No playlists found",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = rememberFontSize(),
            color = AppTheme.SecondaryText
        )
        Spacer(modifier = Modifier.weight(1f))
        return
    }

    val maxWidth = rememberInterfaceMaxWidth()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        if (isEmpty && isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AppTheme.PrimaryColor
            )
            return@Box
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = maxWidth),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            if (state.pinnedPlayList.content.isNotEmpty()) {
                item(key = "pinned_header") {
                    SectionHeader(
                        title = "Pinned",
                        count = state.pinnedPlayList.totalElements,
                        isExpandable = true,
                        isExpanded = pinnedExpanded,
                        onToggle = { pinnedExpanded = !pinnedExpanded }
                    )
                }
                if (pinnedExpanded) {
                    items(state.pinnedPlayList.content, key = { "pinned_${it.id}" }) { item ->
                        PlayListItem(
                            playList = item,
                            onClick = onPlayListClick,
                            isPinned = true,
                            pinEnabled = state.canUsePin,
                            onPinClick = onUnPin
                        )
                    }
                    if (state.pinnedPlayList.isLoading) {
                        item("pinned_loading_more") {
                            LoadingRow()
                        }
                    }
                }
            }

            if (state.otherPlayList.content.isNotEmpty()) {
                item(key = "other_header") {
                    SectionHeader(
                        title = "All Playlists",
                        count = state.otherPlayList.totalElements
                    )
                }
                items(state.otherPlayList.content, key = { "other_${it.id}" }) { item ->
                    PlayListItem(
                        playList = item,
                        onClick = onPlayListClick,
                        isPinned = false,
                        pinEnabled = state.canUsePin,
                        onPinClick = onPin
                    )
                }
                if (state.otherPlayList.isLoading) {
                    item("other_loading_more") {
                        LoadingRow()
                    }
                }
            }
        }
    }

    state.error?.let { error ->
        Text(
            text = "Error: $error",
            modifier = Modifier
                .padding(16.dp),
            color = AppTheme.Error,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Long,
    isExpandable: Boolean = false,
    isExpanded: Boolean = true,
    onToggle: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (isExpandable) it.clickable { onToggle() } else it }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$title ($count)",
            color = AppTheme.SecondaryText,
            fontWeight = FontWeight.SemiBold,
            fontSize = rememberLabelFontSize()
        )
        if (isExpandable) {
            Icon(
                imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (isExpanded) "Collapse pinned" else "Expand pinned",
                tint = AppTheme.SecondaryText
            )
        }
    }
}

@Composable
private fun LoadingRow() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppTheme.PrimaryColor
        )
    }
}
