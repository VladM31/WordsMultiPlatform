package vm.words.ua.exercise.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.exercise.ui.actions.ExerciseSelectAction
import vm.words.ua.exercise.domain.models.data.ExerciseSelection
import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.ui.states.ExerciseSelectState

class ExerciseSelectionViewModel : ViewModel() {

    private val mutableState = MutableStateFlow(ExerciseSelectState())
    val state: StateFlow<ExerciseSelectState> = mutableState

    fun sent(action: ExerciseSelectAction) {
        when (action) {
            is ExerciseSelectAction.AddExercise -> addExercise(action.exercise)
            is ExerciseSelectAction.RemoveExercise -> removeExercise(action.exercise)
            is ExerciseSelectAction.ConfirmSelection -> handleConfirmSelection()
        }
    }

    private fun addExercise(exercise: Exercise) {
        if (state.value.selectedExercises.containsKey(exercise)){
            return
        }
        val newNumber = state.value.number + 1
        val exerciseSelectionIndexed = state.value.exercises.withIndex().first{ it.value.exercise == exercise }

        val newExercises = state.value.exercises.toMutableList().apply {
            removeAt(exerciseSelectionIndexed.index)
            add(
                ExerciseSelection(
                    exercise,
                    newNumber
                )
            )
        }.sorted()
        mutableState.value = state.value.copy(
            exercises = newExercises,
            selectedExercises = state.value.selectedExercises + (exercise to newNumber),
            number = newNumber
        )
    }

    private fun removeExercise(exercise: Exercise) {
        val numberExercise = state.value.selectedExercises[exercise] ?: return
        val newNumber = state.value.number - 1
        val newSelectedExercises = state.value.selectedExercises.toMutableMap().apply {
            remove(exercise)
        }

        val newExercises =  state.value.exercises.toMutableList().apply {
            removeAt(numberExercise - 1)
            add(ExerciseSelection(exercise))
        }

        for (i in 0 until newNumber){
            newExercises[i] = newExercises[i].copy(number = i + 1)
            newSelectedExercises[newExercises[i].exercise] = i + 1
        }


        mutableState.value = state.value.copy(
            exercises = newExercises.sorted(),
            selectedExercises = newSelectedExercises,
            number = newNumber
        )
    }

    private fun handleConfirmSelection() {
        if (state.value.isConfirmed) {
            return
        }

        if (state.value.number == 0) {
            return
        }

        viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = state.value.copy(
                isConfirmed = true
            )
        }
    }

    companion object {
        private const val COMPARATOR_KEY = "number"
    }
}



