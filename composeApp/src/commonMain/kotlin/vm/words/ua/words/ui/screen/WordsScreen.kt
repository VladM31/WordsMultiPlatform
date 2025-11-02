package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

//    // Check for returned filter from PlayListFilterScreen
//    LaunchedEffect(navController.currentRoute) {
//        val returnedFilter = navController.getReturnParam<PlayListCountFilter>()
//        if (returnedFilter != null) {
//            viewModel.sent(PlayListAction.UpdateFilter(returnedFilter))
//        }
//    }

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
            onAdditionalClick = {
//                navController.navigate(Screen.PlayListFilter, state.filter)
            },
            onBackClick = {
                navController.popBackStack()
            },
            additionalButtonImage = painterResource(Res.drawable.find)
        )


        Items(
            content = state.words,
            listState = listState,
            isLoading = state.isLoading,
            toKey = { index -> state.words[index].id },
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
