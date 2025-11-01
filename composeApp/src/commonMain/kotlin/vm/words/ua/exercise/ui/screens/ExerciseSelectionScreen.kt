package vm.words.ua.exercise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.components.CenteredLoader
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.di.rememberInstance
import vm.words.ua.exercise.domain.mappers.toScreen
import vm.words.ua.exercise.ui.actions.ExerciseSelectAction
import vm.words.ua.exercise.ui.bundles.ExerciseSelectionBundle
import vm.words.ua.exercise.ui.bundles.SelectingAnOptionBundle
import vm.words.ua.exercise.ui.componets.ExerciseItemSelected
import vm.words.ua.exercise.ui.componets.ExerciseItemUnselected
import vm.words.ua.exercise.ui.vm.ExerciseSelectionViewModel
import vm.words.ua.navigation.SimpleNavController


@Composable
fun ExerciseSelectionScreen(
    navController: SimpleNavController
) {
    val viewModel: ExerciseSelectionViewModel = rememberInstance<ExerciseSelectionViewModel>()
    val state by viewModel.state.collectAsState()
    val fontSize = getFontSize()
    val bundle = navController.getParamOrThrow<ExerciseSelectionBundle>() // Adjust the type as needed


    LaunchedEffect(state.isConfirmed) {
        if (!state.isConfirmed) {
            return@LaunchedEffect
        }
        if (bundle.transactionId != state.transactionId) {
            return@LaunchedEffect
        }
        val exercises = state.exercises.filter { it.isSelected }
        val bundle = SelectingAnOptionBundle(
            exercises = exercises,
            words = state.words,
            transactionId = state.transactionId,
            isActiveSubscribe = state.isActiveSubscribe,
        )
        navController.navigateAndClearCurrent(exercises.first().exercise.toScreen(), bundle)
    }

    LaunchedEffect(Unit) {
        viewModel.sent(
            ExerciseSelectAction.Init(
                playListId = bundle.playListId,
                words = bundle.words,
                transactionId = bundle.transactionId,
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Select Exercises",
            onBackClick = { navController.popBackStack() }
        )

        if (state.isLoading) {
            // Let the loader fill available space so its internal Column centers content
            CenteredLoader(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                message = "Prepare..."
            )
            return@Column
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Выбранные упражнения
            items(
                state.exercises.filter { it.isSelected }.sortedBy { it.number }
            ) { exerciseSelection ->
                ExerciseItemSelected(
                    exerciseSelection = exerciseSelection,
                    fontSize = fontSize,
                    onRemove = {
                        viewModel.sent(ExerciseSelectAction.RemoveExercise(exerciseSelection.exercise))
                    }
                )
            }

            // Пространство между выбранными и невыбранными
            if (state.selectedExercises.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            // Невыбранные упражнения
            items(
                state.exercises.filter { !it.isSelected }
            ) { exerciseSelection ->
                ExerciseItemUnselected(
                    exerciseSelection = exerciseSelection,
                    fontSize = fontSize,
                    onSelect = {
                        viewModel.sent(ExerciseSelectAction.AddExercise(exerciseSelection.exercise))
                    }
                )
            }
        }

        // Confirm button
        Button(
            onClick = { viewModel.sent(ExerciseSelectAction.ConfirmSelection) },
            enabled = state.exercises.count { it.isSelected } > 0,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)

        ) {
            Text(text = "Confirm",  fontSize = fontSize * 1.2, modifier = Modifier.padding(horizontal = 48.dp))
        }

    }
}
