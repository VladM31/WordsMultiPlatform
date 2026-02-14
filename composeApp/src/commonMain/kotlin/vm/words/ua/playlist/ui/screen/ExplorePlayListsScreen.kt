package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.*
import vm.words.ua.core.ui.states.ToastData
import vm.words.ua.core.utils.rememberFontSize
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

        if (state.playlists.isEmpty() && state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = AppTheme.PrimaryColor
            )
            return@Column
        }


        if (state.playlists.isEmpty() && !state.isLoading) {
            Text(
                text = "No public playlists found",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = rememberFontSize(),
                color = AppTheme.SecondaryText
            )
            return@Column
        }

        Items(
            content = state.playlists,
            listState = listState,
            isLoading = state.isLoading,
            toKey = { content, index -> content[index].id },
            toItem = { _, item ->
                ExplorePlayListItem(
                    playList = item,
                    isAssigning = state.isAssigning,
                    onAssignClick = {
                        viewModel.sent(ExplorePlayListsAction.AssignPlayList(item))
                    },
                    onViewClick = {
                        navController.navigate(
                            Screen.PublicPlayListDetails,
                            PlayListDetailsBundle(item.id)
                        )
                    }
                )
            }
        )

    }
    state.errorMessage?.let {
        ErrorMessageBox(it)
    }
    AppToast(toaster)
}

