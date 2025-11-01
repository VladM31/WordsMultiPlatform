package vm.words.ua.exercise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.exercise.domain.mappers.toScreen
import vm.words.ua.exercise.ui.actions.MatchWordsAction
import vm.words.ua.exercise.ui.bundles.ExerciseBundle
import vm.words.ua.exercise.ui.componets.MatchWordCard
import vm.words.ua.exercise.ui.vm.MatchWordsViewModel
import vm.words.ua.navigation.SimpleNavController

@Composable
fun MatchWordsScreen(
    viewModel: MatchWordsViewModel = rememberInstance<MatchWordsViewModel>(),
    navController: SimpleNavController
) {
    val state = viewModel.state.collectAsState()
    val param = navController.getParamOrThrow<ExerciseBundle>()
    val fontSize = getFontSize()

    LaunchedEffect(Unit) {
        viewModel.sent(
            MatchWordsAction.Init(
                words = param.words,
                transactionId = param.transactionId
            )
        )
    }

    LaunchedEffect(state.value.isEnd) {
        if (!state.value.isEnd) {
            return@LaunchedEffect
        }
        if (state.value.transactionId != param.transactionId){
            return@LaunchedEffect
        }
        if (param.isLast) {
            navController.popBackStack()
            return@LaunchedEffect
        }
        val bundle = param.toNext(state.value.words)
        param.nextExercise.toScreen().let { nextExercise ->
            navController.navigateAndClearCurrent(nextExercise, bundle)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ColorSchema.background)
    ) {
        AppToolBar(
            title = param.currentExercise.text,
            onBackClick = { navController.popBackStack() }
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            // Original words column
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(
                    items = state.value.originalWords,
                    key = { _, item -> item.word.userWordId }
                ) { index, wordBox ->
                    val isSelected = state.value.original?.let {
                        it.id == wordBox.word.userWordId && it.index == index
                    } ?: false

                    MatchWordCard(
                        text = wordBox.word.original,
                        isSelected = isSelected,
                        isMistake = wordBox.isMistake,
                        isMatched = wordBox.position != null,
                        fontSize = fontSize,
                        onClick = {
                            viewModel.sent(
                                MatchWordsAction.Click(
                                    isOriginal = true,
                                    wordId = wordBox.word.userWordId,
                                    index = index
                                )
                            )
                        },
                        enabled = state.value.original == null
                    )
                }
            }

            // Translate words column
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(
                    items = state.value.translateWords,
                    key = { _, item -> item.word.userWordId }
                ) { index, wordBox ->
                    val isSelected = state.value.translate?.let {
                        it.id == wordBox.word.userWordId && it.index == index
                    } ?: false

                    MatchWordCard(
                        text = wordBox.word.translate,
                        isSelected = isSelected,
                        isMistake = wordBox.isMistake,
                        isMatched = wordBox.position != null,
                        fontSize = fontSize,
                        onClick = {
                            viewModel.sent(
                                MatchWordsAction.Click(
                                    isOriginal = false,
                                    wordId = wordBox.word.userWordId,
                                    index = index
                                )
                            )
                        },
                        enabled = state.value.translate == null
                    )
                }
            }
        }
    }
}

