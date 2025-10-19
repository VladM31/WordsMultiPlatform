package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.playlist.ui.states.PlayListState


@Composable
fun ColumnScope.PlayListItems(
    state: PlayListState,
    listState: LazyListState
){
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        if (state.playlists.isEmpty() && state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AppTheme.PrimaryGreen
            )
            return
        }

        if (state.playlists.isEmpty() && !state.isLoading) {
            Text(
                text = "No playlists found",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                color = AppTheme.SecondaryText
            )
            return
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = state.playlists.size,
                key = { index -> state.playlists[index].id }
            ) { index ->
                val playlist = state.playlists[index]
                PlayListItem(
                    playList = playlist,
                    onClick = { playlistId ->
                        // TODO: Navigate to playlist details
                        println("Clicked playlist: $playlistId")
                    }
                )
            }

            if (state.isLoading.not()) {
                return@LazyColumn
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.PrimaryGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        state.error?.let { error ->
            Text(
                text = "Error: $error",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                color = AppTheme.Error,
                fontSize = 14.sp
            )
        }
    }
}