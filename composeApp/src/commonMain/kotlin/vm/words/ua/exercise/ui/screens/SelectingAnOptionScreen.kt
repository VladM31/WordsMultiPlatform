package vm.words.ua.exercise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.CenteredLoader
import vm.words.ua.core.ui.components.ImageFromBytes
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getImageSize
import vm.words.ua.core.utils.getWidthDeviceFormat
import vm.words.ua.di.rememberInstance
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.ui.actions.SelectingAnOptionAction
import vm.words.ua.exercise.ui.bundles.ExerciseBundle
import vm.words.ua.exercise.ui.componets.OptionCard
import vm.words.ua.exercise.ui.effects.EndExerciseEffect
import vm.words.ua.exercise.ui.utils.isSoundAfterAnswer
import vm.words.ua.exercise.ui.utils.isSoundBeforeAnswer
import vm.words.ua.exercise.ui.utils.toOptionText
import vm.words.ua.exercise.ui.utils.toText
import vm.words.ua.exercise.ui.vm.SelectingAnOptionVm
import vm.words.ua.navigation.SimpleNavController
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.image_icon

@Composable
fun WordByTranslatesScreen(navController: SimpleNavController) {
    SelectingAnOptionScreen(navController = navController)
}

@Composable
fun WordByOriginalsScreen(navController: SimpleNavController) {
    SelectingAnOptionScreen(navController = navController)
}

@Composable
fun WordByDescriptionsScreen(navController: SimpleNavController) {
    SelectingAnOptionScreen(navController = navController)
}

@Composable
fun DescriptionByWordsScreen(navController: SimpleNavController) {
    SelectingAnOptionScreen(navController = navController)
}


@Composable
private fun SelectingAnOptionScreen(
    viewModel: SelectingAnOptionVm = rememberInstance<SelectingAnOptionVm>(),
    navController: SimpleNavController
) {
    val scroll = rememberScrollState()
    val state = viewModel.state.collectAsState()
    val isPhoneFormat = getWidthDeviceFormat().isPhone
    val param = navController.getParamOrThrow<ExerciseBundle>()
    val fontSize = getFontSize()

    val toText = { word: ExerciseWordDetails ->
        word.toText(state.value.exercise)
    }
    val toOption = { word: ExerciseWordDetails ->
        word.toOptionText(state.value.exercise)
    }
    val onNext = {
        viewModel.sent(SelectingAnOptionAction.Next)
    }
    val onChoose: (ExerciseWordDetails) -> Unit = {
        viewModel.sent(SelectingAnOptionAction.ChooseWord(it))
    }

    LaunchedEffect(Unit) {
        val exerciseType = param.exercises[param.number].exercise
        viewModel.sent(
            SelectingAnOptionAction.Init(
                words = param.words,
                exerciseType = exerciseType,
                transactionId = param.transactionId,
                isActiveSubscribe = param.isActiveSubscribe,
                isSoundBeforeAnswer = exerciseType.isSoundBeforeAnswer(),
                isSoundAfterAnswer = exerciseType.isSoundAfterAnswer()
            )
        )
    }

    EndExerciseEffect(state.value, param, navController)

    // Left pane: image (if allowed) + main word
    val imageContent = @Composable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (state.value.enableImage()) {
                val imageSize = getImageSize()
                ImageFromBytes(
                    imageBytes = state.value.currentWord().imageContent?.bytes,
                    defaultPaint = painterResource(Res.drawable.image_icon),
                    width = imageSize,
                    height = imageSize,
                    contentDescription = "Word Image"
                )
                Spacer(Modifier.height(10.dp))
            }

            Text(
                text = toText(state.value.currentWord()),
                color = AppTheme.PrimaryColor,
                textAlign = TextAlign.Center,
                fontSize = fontSize,
                style = LocalTextStyle.current.copy(lineHeight = fontSize * 1.1f),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }
    }

    // Right pane: options
    val optionContent = @Composable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Варианты ответа
            state.value.wordsToChoose.take(3).forEach { opt ->
                val isRight = state.value.isCorrect == true && state.value.currentWord().wordId == opt.wordId
                val isWrong = state.value.isCorrect == false && state.value.currentWord().wordId != opt.wordId

                OptionCard(
                    text = toOption(opt),
                    isRight = isRight,
                    isWrong = isWrong,
                    fontSize = fontSize,
                    onClick = { onChoose(opt) },
                    enabled = state.value.waitNext.not()
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))
        }
    }



    Box(
        Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
            .padding(bottom = 16.dp)
    ) {
        // Choose columns count based on device
        val columns = if (isPhoneFormat) 1 else 2

        Column(modifier = Modifier.fillMaxSize()) {
            // App toolbar
            AppToolBar(
                title = param.currentExercise.text,
                onBackClick = { navController.popBackStack() }
            )

            // Loading state — show centered loader and stop further composition of content
            if (state.value.isInited.not()) {
                // Let the loader fill available space so its internal Column centers content
                CenteredLoader(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    message = "Prepare..."
                )
                return@Column
            }

            TwoPaneGrid(
                columns = columns,
                scroll = scroll,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                left = {
                    // On phone we want the options to scroll with whole screen -> use the same scroll state
                    if (columns == 1) {
                        Column(
                            modifier = Modifier.verticalScroll(scroll),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            imageContent()
                            Spacer(Modifier.height(12.dp))
                            optionContent()
                        }
                    } else {
                        imageContent()
                    }
                },
                right = {
                    // For two-column layout the right column already has verticalScroll applied in TwoPaneGrid
                    if (columns == 1) {
                        // no-op, handled above
                    } else {
                        optionContent()
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        if (state.value.waitNext.not()) {
            return@Box
        }

        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(Modifier.background(AppTheme.PrimaryBack).fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter){
                Button(onClick = onNext, modifier = Modifier.padding(16.dp).background(AppTheme.PrimaryBack),) {
                    Text("Next", fontSize = fontSize, modifier = Modifier.padding(horizontal = 32.dp))
                }
            }
        }
    }
}


@Composable
fun TwoPaneGrid(
    columns: Int,
    modifier: Modifier = Modifier,
    left: @Composable () -> Unit,
    right: @Composable () -> Unit,
    scroll: androidx.compose.foundation.ScrollState
) {
    if (columns <= 1) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            left()
            Spacer(Modifier.height(12.dp))
            right()
        }
        return
    }
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            left()
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
                .verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            right()
        }
    }

}