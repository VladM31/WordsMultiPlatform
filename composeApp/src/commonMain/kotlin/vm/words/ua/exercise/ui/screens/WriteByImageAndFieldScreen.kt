package vm.words.ua.exercise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.getWidthDeviceFormat
import vm.words.ua.di.rememberInstance
import vm.words.ua.exercise.ui.actions.WriteByImageAndFieldAction
import vm.words.ua.exercise.ui.bundles.ExerciseBundle
import vm.words.ua.exercise.ui.componets.ExerciseImageView
import vm.words.ua.exercise.ui.componets.NextButton
import vm.words.ua.exercise.ui.componets.WordInputPanel
import vm.words.ua.exercise.ui.componets.calcNextButtonHeight
import vm.words.ua.exercise.ui.effects.EndExerciseEffect
import vm.words.ua.exercise.ui.states.WriteByImageAndFieldState
import vm.words.ua.exercise.ui.utils.toText
import vm.words.ua.exercise.ui.vm.WriteByImageAndFieldVm
import vm.words.ua.navigation.SimpleNavController


@Composable
fun WriteByImageAndTranslationScreen(
    viewModel: WriteByImageAndFieldVm = rememberInstance<WriteByImageAndFieldVm>(),
    navController: SimpleNavController,
) {
    WriteByImageAndFieldScreen(
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
fun WriteByImageAndDescriptionScreen(
    viewModel: WriteByImageAndFieldVm = rememberInstance<WriteByImageAndFieldVm>(),
    navController: SimpleNavController,
) {
    WriteByImageAndFieldScreen(
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
private fun WriteByImageAndFieldScreen(
    viewModel: WriteByImageAndFieldVm,
    navController: SimpleNavController,
) {
    val state = viewModel.state.collectAsState()
    val param = navController.getParamOrThrow<ExerciseBundle>()
    val fontSize = getFontSize()


    LaunchedEffect(Unit) {
        val exerciseType = param.exercises[param.number].exercise
        viewModel.sent(
            WriteByImageAndFieldAction.Init(
                words = param.words,
                isActiveSubscribe = param.isActiveSubscribe,
                transactionId = param.transactionId,
                exercise = exerciseType
            )
        )
    }

    EndExerciseEffect(state.value, param, navController)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(bottom = calcNextButtonHeight())) {
            AppToolBar(
                title = param.currentExercise.text,
                onBackClick = { navController.popBackStack() }
            )

            // Content area takes remaining space and bounds inner scrollable content
            WriteByImageAndFieldContent(
                state, fontSize, viewModel,
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp)
            )
        }

        NextButton(
            hide = false,
            text = if (state.value.isConfirm == true) "Next" else "Confirm",
            onNextClick = {
                val action = if (state.value.isConfirm == true) {
                    WriteByImageAndFieldAction.NextWord
                } else {
                    WriteByImageAndFieldAction.Confirm
                }
                viewModel.sent(action)
            }
        )
    }
}

@Composable
private fun WriteByImageAndFieldContent(
    state: State<WriteByImageAndFieldState>,
    fontSize: TextUnit,
    viewModel: WriteByImageAndFieldVm,
    modifier: Modifier = Modifier
) {
    if (state.value.isInited.not()) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
        return
    }

    val columns = if (getWidthDeviceFormat().isPhone) 1 else 2
    // helper: во всю ширину только если 1 колонка
    val span: (LazyGridItemSpanScope) -> GridItemSpan = {
        if (columns == 1) GridItemSpan(it.maxLineSpan) else GridItemSpan(1)
    }
    val scale = getScaleFactor()
    val minHeightModifier = remember(scale) {
        if (columns == 1) {
            Modifier
        } else {
            Modifier.heightIn(min = 300.dp * scale)
        }

    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
    ) {
        item(span = span) {
            Box(modifier = minHeightModifier, contentAlignment = Alignment.Center) {
                ExerciseImageView(
                    enableImage = state.value.enableImage(),
                    word = state.value.currentWord(),
                    fontSize = fontSize
                ) {
                    it.toText(state.value.exercise)
                }
            }
        }

        item(span = span) {
            Box(modifier = minHeightModifier, contentAlignment = Alignment.Center) {
                WordInputPanel(
                    text = state.value.wordText ?: "",
                    onTextChange = { viewModel.sent(WriteByImageAndFieldAction.UpdateText(it)) },
                    enabled = state.value.isEditEnable,
                    onAddLetter = { viewModel.sent(WriteByImageAndFieldAction.AddLetter) }
                )
            }
        }
    }
}
