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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.BottomNavBar
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.ui.actions.PlayListAction
import vm.words.ua.playlist.ui.components.PlayListItem
import vm.words.ua.playlist.ui.components.PlayListItems
import vm.words.ua.playlist.ui.vms.PlayListViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.find

@Composable
fun PlayListScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PlayListViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Check for returned filter from PlayListFilterScreen
    LaunchedEffect(navController.currentRoute) {
        val returnedFilter = navController.getReturnParam<PlayListCountFilter>()
        if (returnedFilter != null) {
            viewModel.sent(PlayListAction.UpdateFilter(returnedFilter))
        }
    }

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
            showBackButton = false,
            showAdditionalButton = true,
            onAdditionalClick = {
                navController.navigate(Screen.PlayListFilter, state.filter)
            },
            additionalButtonImage = painterResource(Res.drawable.find)
        )

        PlayListItems(
            state = state,
            listState = listState,
        )

        BottomNavBar(
            currentRoute = Screen.PlayList,
            onNavigate = { route -> navController.navigate(route) }
        )
    }
}
