package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.Items
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.ui.actions.WordsAction
import vm.words.ua.words.ui.bundles.WordDetailsBundle
import vm.words.ua.words.ui.bundles.WordFilterBundle
import vm.words.ua.words.ui.components.WordItem
import vm.words.ua.words.ui.vms.WordsViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.find

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
            additionalButtonImage = painterResource(Res.drawable.find)
        )

        Items(
            content = state.words,
            listState = listState,
            isLoading = state.isLoading,
            toKey = { content, index -> content[index].id },
            toItem = { index, item ->
                WordItem(
                    word = item,
                    isSelected = state.selectedWords.contains(item.id),
                    onSelect = {
                        viewModel.sent(WordsAction.SelectWord(item.id))
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


    }
}
