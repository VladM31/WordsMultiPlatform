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
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.bundles.PlayListDetailsBundle
import vm.words.ua.playlist.ui.actions.PublicPlayListDetailsAction
import vm.words.ua.playlist.ui.components.ReadOnlyWordItem
import vm.words.ua.playlist.ui.vms.PublicPlayListDetailsViewModel

@Composable
fun PublicPlayListDetailsScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<PublicPlayListDetailsViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    val bundle = navController.getParam<PlayListDetailsBundle>()

    LaunchedEffect(bundle) {
        bundle?.let {
            viewModel.sent(PublicPlayListDetailsAction.Init(it.playListId))
        }
    }

    val playList = state.playList
    val title = playList?.name ?: "Loading..."

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = title,
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.isLoading && playList == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppTheme.PrimaryColor
                )
                return@Box
            }
            if (state.errorMessage != null) {
                return@Box
            }
            if (playList != null && playList.words.isEmpty()) {
                Text(
                    text = "No words in this playlist",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp,
                    color = AppTheme.SecondaryText
                )
                return@Box
            }
            if (playList == null) {
                return@Box
            }
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    count = playList.words.size,
                    key = { index -> playList.words[index].userWord.word.id }
                ) { index ->
                    val word = playList.words[index].userWord.word
                    ReadOnlyWordItem(word = word, navController = navController)
                }
            }


        }
        state.errorMessage?.let {
            ErrorMessageBox(it)
        }
    }
}

