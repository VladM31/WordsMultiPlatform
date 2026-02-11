package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToast
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.rememberToast
import vm.words.ua.core.ui.states.ToastData
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.bundles.PlayListDetailsBundle
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter
import vm.words.ua.playlist.ui.actions.ExplorePlayListsAction
import vm.words.ua.playlist.ui.components.ExplorePlayListItem
import vm.words.ua.playlist.ui.vms.ExplorePlayListsViewModel

@Composable
fun ExplorePlayListsScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<ExplorePlayListsViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val toaster = rememberToast()

    val toastData = remember(state.lastAssignedPlayList) {
        if (state.lastAssignedPlayList == null) {
            return@remember null
        }
        return@remember ToastData(
            message = "Playlist '${state.lastAssignedPlayList?.name}' added to your playlists",
            buttonText = "Open",
            onButtonClick = {
                navController.navigateAndClear(Screen.PlayList)
            },
            duration = ToastData.Duration.Long
        )

    }

    LaunchedEffect(toastData) {
        toastData?.let {
            toaster.show(it)
        }
    }

    // Check for returned filter from filter screen
    LaunchedEffect(navController.currentRoute) {
        val returnValue = navController.getReturnParam<Any>() ?: return@LaunchedEffect
        (returnValue as? PublicPlayListCountFilter)?.let {
            viewModel.sent(ExplorePlayListsAction.UpdateFilter(it))
        }
    }

    // Load more when reaching end of list
    LaunchedEffect(listState.canScrollForward, state.isLoading) {
        if (!listState.canScrollForward && !state.isLoading && state.hasMore) {
            viewModel.sent(ExplorePlayListsAction.LoadMore)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Explore Playlists",
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = true,
            onAdditionalClick = {
                navController.navigate(Screen.ExplorePlayListsFilter, state.filter)
            },
            additionalButtonVector = Icons.Outlined.Search
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.playlists.isEmpty() && state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppTheme.PrimaryColor
                )
                return@Box
            }


            if (state.playlists.isEmpty() && !state.isLoading) {
                Text(
                    text = "No public playlists found",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp,
                    color = AppTheme.SecondaryText
                )
                return@Box
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
                    ExplorePlayListItem(
                        playList = playlist,
                        showCreatedDate = state.showCreatedDate,
                        isAssigning = state.isAssigning,
                        onAssignClick = {
                            viewModel.sent(ExplorePlayListsAction.AssignPlayList(playlist))
                        },
                        onViewClick = {
                            navController.navigate(
                                Screen.PublicPlayListDetails,
                                PlayListDetailsBundle(playlist.id)
                            )
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
                            color = AppTheme.PrimaryColor,
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
    AppToast(toaster)
}

