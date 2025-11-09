package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.Items
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.ui.actions.UserWordsAction
import vm.words.ua.words.ui.bundles.UserWordFilterBundle
import vm.words.ua.words.ui.bundles.WordDetailsBundle
import vm.words.ua.words.ui.components.SelectionBottomMenu
import vm.words.ua.words.ui.components.WordItem
import vm.words.ua.words.ui.vms.UserWordsViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.find

@Composable
fun UserWordsScreen(
    navController: SimpleNavController
) {
    val viewModel = rememberInstance<UserWordsViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Pull-to-refresh behavior when scrolled to top and a trigger occurs
    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                if (!state.isLoading && state.page > 0) {
                    viewModel.sent(UserWordsAction.ReFetch)
                }
            }
    }

    // Load more when reaching end of list
    LaunchedEffect(listState.canScrollForward, state.isLoading) {
        if (!listState.canScrollForward && !state.isLoading && state.hasMore) {
            viewModel.sent(UserWordsAction.LoadMore)
        }
    }

    LaunchedEffect(navController.currentRoute) {
        navController.getReturnParam<UserWordFilterBundle>()?.let {
            viewModel.sent(
                UserWordsAction.UpdateFilter(
                    filter = it.filter
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "User Words",
            showBackButton = true,
            showAdditionalButton = true,
            onAdditionalClick = {
                navController.navigate(
                    Screen.UserWordsFilter,
                    UserWordFilterBundle(state.filter)
                )
            },
            onBackClick = { navController.popBackStack() },
            additionalButtonImage = painterResource(Res.drawable.find)
        )

        Items(
            content = state.userWords,
            listState = listState,
            isLoading = state.isLoading,
            toKey = { content, index -> content[index].id },
            toItem = { _, item ->
                WordItem(
                    word = item.word,
                    userWord = item,
                    isSelected = state.selectedWords.contains(item.id),
                    onSelect = {
                        viewModel.sent(UserWordsAction.SelectWord(item.id))
                    },
                    onOpen = {
                        navController.navigate(
                            Screen.WordDetails,
                            WordDetailsBundle(
                                word = item.word,
                                userWord = item
                            )
                        )
                    }
                )
            }
        )
        if (state.selectedWords.isEmpty()) {
            return
        }

        if (state.selectedWords.isNotEmpty()) {
            SelectionBottomMenu(
                visible = true,
                onUnselect = { viewModel.sent(UserWordsAction.Clear) },
                onApply = { /* open playlist choose later */ },
                showDelete = true,
                deleteLabel = "Delete(${state.selectedWords.size})",
                onDelete = { /* implement delete later */ },
                applyLabel = "Apply(${state.selectedWords.size})"
            )
        }

        state.errorMessage?.let {
            ErrorMessageBox(it)
        }
    }
}
