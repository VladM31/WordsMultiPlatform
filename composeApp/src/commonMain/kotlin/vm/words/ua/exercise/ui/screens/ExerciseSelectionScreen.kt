package vm.words.ua.exercise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.exercise.ui.actions.ExerciseSelectAction
import vm.words.ua.exercise.ui.vm.ExerciseSelectionViewModel
import vm.words.ua.exercise.ui.componets.ExerciseItemSelected
import vm.words.ua.exercise.ui.componets.ExerciseItemUnselected
import vm.words.ua.navigation.SimpleNavController


@Composable
fun ExerciseSelectionScreen(
    viewModel: ExerciseSelectionViewModel = viewModel(),
    navController: SimpleNavController
) {
    val state by viewModel.state.collectAsState()
    val fontSize = getFontSize()


    LaunchedEffect(state.isConfirmed) {
        if (state.isConfirmed) {
            navController.popBackStack()
        }
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                    Spacer(modifier = Modifier.height(8.dp))
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
    }
}
