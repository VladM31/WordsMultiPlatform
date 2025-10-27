package vm.words.ua.exercise.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
// Use local shims for slide transitions
import vm.words.ua.core.ui.animation.slideInUp
import vm.words.ua.core.ui.animation.slideOutDown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.exercise.ui.actions.ExerciseSelectAction
import vm.words.ua.exercise.ui.componets.ExerciseItemSelected
import vm.words.ua.exercise.ui.componets.ExerciseItemUnselected
import vm.words.ua.exercise.ui.vm.ExerciseSelectionViewModel
import vm.words.ua.navigation.SimpleNavController



@Composable
fun ExerciseSelectionScreen(
    viewModel: ExerciseSelectionViewModel = viewModel(),
    navController: SimpleNavController
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isConfirmed) {
        if (state.isConfirmed) {
            navController.popBackStack()
        }
    }

    val sortedExercises = remember(state.exercises) {
        state.exercises.sortedWith(compareByDescending<vm.words.ua.exercise.domain.models.data.ExerciseSelection> { it.isSelected }
            .thenBy { it.number })
    }
    val selectedCount = remember(state.exercises) { state.exercises.count { it.isSelected } }

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
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            itemsIndexed(
                items = sortedExercises,
                key = { _, it -> it.exercise.ordinal }
            ) { index, exerciseSelection ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    // Selected representation
                    AnimatedVisibility(
                        visible = exerciseSelection.isSelected,
                        enter = slideInUp(animationSpec = androidx.compose.animation.core.tween(300)) +
                                fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) +
                                scaleIn(animationSpec = androidx.compose.animation.core.tween(300)),
                        exit = slideOutDown(animationSpec = androidx.compose.animation.core.tween(300)) +
                               fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) +
                               scaleOut(animationSpec = androidx.compose.animation.core.tween(300))
                    ) {
                        ExerciseItemSelected(
                            exerciseSelection = exerciseSelection,
                            onRemove = {
                                viewModel.sent(ExerciseSelectAction.RemoveExercise(exerciseSelection.exercise))
                            }
                        )
                    }

                    // Unselected representation
                    AnimatedVisibility(
                        visible = !exerciseSelection.isSelected,
                        enter = slideInUp(animationSpec = androidx.compose.animation.core.tween(300)) +
                                fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) +
                                scaleIn(animationSpec = androidx.compose.animation.core.tween(300)),
                        exit = slideOutDown(animationSpec = androidx.compose.animation.core.tween(300)) +
                               fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) +
                               scaleOut(animationSpec = androidx.compose.animation.core.tween(300))
                    ) {
                        ExerciseItemUnselected(
                            exerciseSelection = exerciseSelection,
                            onSelect = {
                                viewModel.sent(ExerciseSelectAction.AddExercise(exerciseSelection.exercise))
                            }
                        )
                    }

                    // Spacer between selected and unselected blocks (only once)
                    if (exerciseSelection.isSelected && index == selectedCount - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
