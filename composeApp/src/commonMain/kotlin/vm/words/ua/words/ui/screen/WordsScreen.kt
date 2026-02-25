package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.Items
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.ui.actions.WordsAction
import vm.words.ua.words.ui.bundles.PinUserWordsBundle
import vm.words.ua.words.ui.bundles.WordDetailsBundle
import vm.words.ua.words.ui.bundles.WordFilterBundle
import vm.words.ua.words.ui.components.SelectionBottomMenu
import vm.words.ua.words.ui.components.WordItem
import vm.words.ua.words.ui.vms.WordsViewModel

@Composable
fun WordsScreen(
    navController: SimpleNavController
) {
    val viewModel = rememberInstance<WordsViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val navigateToFilter = remember {
        {
            val bundle = WordFilterBundle(
                filterId = state.filterId,
                filter = state.filter
            )
            navController.navigate(Screen.WordFilter, bundle)
        }
    }

    // Check for returned filter from WordFilterScreen
    LaunchedEffect(navController.currentRoute) {
        navController.getReturnParam<WordFilterBundle>()?.let {
            println("WordsScreen: Received filter from WordFilterScreen: ${it.filter}")
            viewModel.sent(
                WordsAction.UpdateFilter(
                    filter = it.filter
                )
            )
        }
    }

    // Load more when reaching end of list
    LaunchedEffect(listState.canScrollForward, state.isLoading) {
        if (!listState.canScrollForward && !state.isLoading && state.hasMore) {
            viewModel.sent(WordsAction.LoadMore)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Words",
            showBackButton = true,
            showAdditionalButton = true,
            onAdditionalClick = navigateToFilter,
            onBackClick = {
                navController.popBackStack()
            },
            additionalButtonVector = Icons.Outlined.Search
        )

        Items(
            content = state.words,
            listState = listState,
            isLoading = state.isLoading,
            toKey = { content, index -> content[index].id },
            toItem = { _, item ->
                WordItem(
                    word = item,
                    isSelected = state.selectedWords.contains(item.id),
                    onSelect = {
                        viewModel.sent(WordsAction.SelectWord(item))
                    },
                    onOpen = {
                        navController.navigate(
                            Screen.WordDetails,
                            WordDetailsBundle(
                                word = item
                            )
                        )
                    }
                )
            }
        )
        if (state.selectedWords.isEmpty()) {
            return
        }
        SelectionBottomMenu(
            visible = true,
            onUnselect = {
                viewModel.sent(WordsAction.Clear)
            },
            onApply = {
                navController.navigate(
                    Screen.PinUserWords,
                    PinUserWordsBundle(
                        words = state.selectedWords.values
                    )
                )
            },
            applyLabel = "Apply(${state.selectedWords.size})"
        )

    }
}
