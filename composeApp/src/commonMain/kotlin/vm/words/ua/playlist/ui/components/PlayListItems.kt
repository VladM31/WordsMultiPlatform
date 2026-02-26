package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.Items
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.playlist.ui.states.PlayListState


@Composable
fun ColumnScope.PlayListItems(
    state: PlayListState,
    listState: LazyListState,
    onPlayListClick: (String) -> Unit = {}
) {
    if (state.playlists.isEmpty() && !state.isLoading) {
        Text(
            text = "No playlists found",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = rememberFontSize(),
            color = AppTheme.SecondaryText
        )
        Spacer(modifier = Modifier.weight(1f))
        return
    }

    Items(
        content = state.playlists,
        listState = listState,
        isLoading = state.isLoading,
        toKey = { list, index -> list[index].id },
        toItem = { _, item ->
            PlayListItem(
                playList = item,
                onClick = onPlayListClick
            )
        }
    )

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