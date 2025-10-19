package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.ui.components.PlayListItem
import vm.words.ua.playlist.ui.vms.PlayListViewModel

@Composable
fun PlayListScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PlayListViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Load more when reaching end of list
    LaunchedEffect(listState.canScrollForward, state.isLoading) {
        if (!listState.canScrollForward && !state.isLoading && state.hasMore) {
            viewModel.loadMore()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Playlists",
            showBackButton = false
        )

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
            } else if (state.playlists.isEmpty() && !state.isLoading) {
                Text(
                    text = "No playlists found",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp,
                    color = AppTheme.SecondaryText
                )
            } else {
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

                    if (state.isLoading) {
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

        BottomNavBar(
            currentRoute = Screen.PlayList,
            onNavigate = { route -> navController.navigate(route) }
        )
    }
}
