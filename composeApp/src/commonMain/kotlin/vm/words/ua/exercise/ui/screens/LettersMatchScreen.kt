package vm.words.ua.exercise.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExposurePlus1
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberScaleFactor
import vm.words.ua.core.utils.rememberWidthDeviceFormat
import vm.words.ua.di.rememberInstance
import vm.words.ua.exercise.ui.actions.LettersMatchAction
import vm.words.ua.exercise.ui.bundles.ExerciseBundle
import vm.words.ua.exercise.ui.componets.NextButton
import vm.words.ua.exercise.ui.effects.EndExerciseEffect
import vm.words.ua.exercise.ui.states.LettersMatchState
import vm.words.ua.exercise.ui.utils.toText
import vm.words.ua.exercise.ui.vm.LettersMatchVm
import vm.words.ua.navigation.SimpleNavController

@Composable
fun LetterMatchByDescriptionScreen(
    navController: SimpleNavController,
    viewModel: LettersMatchVm = rememberInstance<LettersMatchVm>()
){
    LettersMatchScreen(
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
fun LetterMatchByTranslationScreen(
    navController: SimpleNavController,
    viewModel: LettersMatchVm = rememberInstance<LettersMatchVm>()
){
    LettersMatchScreen(
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
private fun LettersMatchScreen(
    viewModel: LettersMatchVm,
    navController: SimpleNavController,
) {
    val state = viewModel.state.collectAsState()
    val param = navController.getParamOrThrow<ExerciseBundle>()
    val fontSize = rememberFontSize()


    LaunchedEffect(Unit) {
        val exerciseType = param.exercises[param.number].exercise
        viewModel.sent(
            LettersMatchAction.Init(
                words = param.words,
                isActiveSubscribe = param.isActiveSubscribe,
                transactionId = param.transactionId,
                exerciseType = exerciseType
            )
        )
    }

    EndExerciseEffect(state.value, param, navController)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppToolBar(
                title = param.currentExercise.text,
                onBackClick = { navController.popBackStack() },
                additionalButtonVector = Icons.Outlined.ExposurePlus1,
                onAdditionalClick = {
                    viewModel.sent(LettersMatchAction.PlusOneLetter)
                },
                showAdditionalButton = true
            )

            // Content area takes remaining space and bounds inner scrollable grid
            Content(state, fontSize, viewModel, modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(10.dp))
        }

        NextButton(
            hide = state.value.isNext.not(),
            onNextClick = { viewModel.sent(LettersMatchAction.Next) }
        )
    }


}

@Composable
private fun Content(
    state: State<LettersMatchState>,
    fontSize: TextUnit,
    viewModel: LettersMatchVm,
    modifier: Modifier = Modifier
) {
    if (state.value.isInited.not()){
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
        return
    }


    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = state.value.currentWord().toText(state.value.exercise),
                color = AppTheme.PrimaryColor,
                fontSize = fontSize,
                textAlign = TextAlign.Center,
                lineHeight = fontSize * 1.1f,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }

        item {
            Text(text = state.value.resultWord, fontSize = fontSize * 1.2f, color = AppTheme.PrimaryColor)
        }

        item {
            LettersGrid(
                fontSize = fontSize,
                letters = state.value.letters,
                errorLetter = state.value.errorLetter,
                onClick = { letter, id ->
                    viewModel.sent(LettersMatchAction.ClickOnLetter(letter, id))
                }
            )
        }
    }
}


@Composable
private fun LetterItem(letter: String, fontSize: TextUnit, scale: Float, isError: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    val widthDeviceFormat = rememberWidthDeviceFormat()
    val deviceScale = remember(widthDeviceFormat) {
        if (widthDeviceFormat.isPhone){
            1f
        } else {
            1.4f
        }
    }
    val width = remember(deviceScale){
        48.dp * scale * deviceScale
    }
    val height = remember(deviceScale){
        56.dp * scale
    }

    val errorColor = androidx.compose.ui.graphics.Color.Red
    val primaryColor = AppColors.primaryColor
    val color = if (isError) errorColor else primaryColor
    println("LetterItem: letter=$letter, isError=$isError, color=$color, errorColor=$errorColor, primaryColor=$primaryColor")

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .width(width)
            .height(height)
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = AppTheme.PrimaryBack,
        border = BorderStroke(1.dp, color),
        shadowElevation = 2.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = letter,
                fontSize = fontSize * 1.3f,
                color = color,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
private fun LettersGrid(
    fontSize: TextUnit,
    errorLetter: LettersMatchState.ErrorLetter?,
    letters: List<LettersMatchState.Letter>,
    onClick: (Char, String) -> Unit,
) {
    val scale = rememberScaleFactor()
    LazyVerticalGrid(columns = GridCells.Adaptive(56.dp), modifier = Modifier.fillMaxSize().heightIn(max = 400.dp)) {
        items(
            count = letters.size,
            key = { idx -> letters[idx].id }
        ) { idx ->
            val l = letters[idx]
            val isError = errorLetter?.letter?.id == l.id
            LetterItem(
                letter = l.letter.toString(),
                fontSize = fontSize,
                isError = isError,
                scale = scale
            ) {
                onClick(l.letter, l.id)
            }
        }
    }
}
