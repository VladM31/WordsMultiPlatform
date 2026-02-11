package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.bundles.PlayListDetailsBundle
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.ui.actions.PlayListAction
import vm.words.ua.playlist.ui.components.CreatePlayListDialog
import vm.words.ua.playlist.ui.components.PlayListItems
import vm.words.ua.playlist.ui.vms.PlayListViewModel

@Composable
fun PlayListScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PlayListViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    var showCreateDialog by remember { mutableStateOf(false) }

    // Check for returned filter from PlayListFilterScreen
    LaunchedEffect(navController.currentRoute) {
        val returnValue = navController.getReturnParam<Any>() ?: return@LaunchedEffect
        (returnValue as? PlayListCountFilter)?.let {
            viewModel.sent(PlayListAction.UpdateFilter(it))
        }
        (returnValue as? PlayListAction)?.let {
            viewModel.sent(action = it)
        }
    }

    // Load more when reaching end of list
    LaunchedEffect(listState.canScrollForward, state.isLoading) {
        if (!listState.canScrollForward && !state.isLoading && state.hasMore) {
            viewModel.sent(PlayListAction.LoadMore)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Playlists",
            showBackButton = true,
            onBackClick = { showCreateDialog = true },
            backButtonVector = Icons.Outlined.Add,
            showAdditionalButton = true,
            onAdditionalClick = {
                navController.navigate(Screen.PlayListFilter, state.filter)
            },
            additionalButtonVector = Icons.Outlined.Search
        )

        if (showCreateDialog) {
            CreatePlayListDialog(
                onDismiss = { showCreateDialog = false },
                onCreate = { name ->
                    viewModel.sent(PlayListAction.Create(name))
                }
            )
        }

        PlayListItems(
            state = state,
            listState = listState,
            onPlayListClick = { playListId ->
                navController.navigate(Screen.PlayListDetails, PlayListDetailsBundle(playListId))
            }
        )

        BottomNavBar(
            currentRoute = Screen.PlayList,
            onNavigate = { route -> navController.navigateAndClear(route) }
        )
    }
}
