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
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.exercise.ui.actions.MatchWordsAction
import vm.words.ua.exercise.ui.bundles.ExerciseBundle
import vm.words.ua.exercise.ui.componets.ExerciseProgressBar
import vm.words.ua.exercise.ui.componets.MatchWordCard
import vm.words.ua.exercise.ui.effects.EndExerciseEffect
import vm.words.ua.exercise.ui.vm.MatchWordsViewModel
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.navigation.rememberParamOrThrow

@Composable
fun MatchWordsScreen(
    viewModel: MatchWordsViewModel = rememberInstance<MatchWordsViewModel>(),
    navController: SimpleNavController
) {
    val state = viewModel.state.collectAsState()
    val param = navController.rememberParamOrThrow<ExerciseBundle>()
    val fontSize = rememberFontSize()

    LaunchedEffect(Unit) {
        viewModel.sent(
            MatchWordsAction.Init(
                words = param.words,
                transactionId = param.transactionId
            )
        )
    }

    EndExerciseEffect(state.value, param, navController)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.ColorScheme.background)
    ) {
        AppToolBar(
            title = param.currentExercise.text,
            onBackClick = { navController.popBackStack() },
            onRightSwipe = {
                if (state.value.isEnd) {
                    navController.popBackStack()
                }
            }
        )

        ExerciseProgressBar(state = state.value, bundle = param)

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
                                    word =  wordBox.word,
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
                                    word = wordBox.word,
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

