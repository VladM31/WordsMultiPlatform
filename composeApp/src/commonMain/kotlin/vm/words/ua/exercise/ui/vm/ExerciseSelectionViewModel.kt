package vm.words.ua.exercise.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.exercise.domain.mappers.toExerciseWordDetails
import vm.words.ua.exercise.ui.actions.ExerciseSelectAction
import vm.words.ua.exercise.domain.models.data.ExerciseSelection
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.ui.states.ExerciseSelectState
import vm.words.ua.subscribes.domain.managers.SubscribeCacheManager

class ExerciseSelectionViewModel(
    private val subscribeCacheManager: SubscribeCacheManager,
    private val byteContentManager: ByteContentManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(ExerciseSelectState())
    val state: StateFlow<ExerciseSelectState> = mutableState

    fun sent(action: ExerciseSelectAction) {
        when (action) {
            is ExerciseSelectAction.AddExercise -> addExercise(action.exercise)
            is ExerciseSelectAction.RemoveExercise -> removeExercise(action.exercise)
            is ExerciseSelectAction.ConfirmSelection -> handleConfirmSelection()
            is ExerciseSelectAction.Init -> init(action)
        }
    }

    private fun init(action: ExerciseSelectAction.Init){
        if (state.value.isInited){
            return
        }
        mutableState.value = state.value.copy(
            isInited = true,
            words = action.words.map { it.toExerciseWordDetails(state.value.transactionId) },
        )
    }

    private fun addExercise(exercise: Exercise) {
        if (state.value.selectedExercises.containsKey(exercise)) {
            return
        }
        val newNumber = state.value.number + 1
        val exerciseSelectionIndexed = state.value.exercises.withIndex().first { it.value.exercise == exercise }

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

        val newExercises = state.value.exercises.toMutableList().apply {
            removeAt(numberExercise - 1)
            add(ExerciseSelection(exercise))
        }

        for (i in 0 until newNumber) {
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
        mutableState.value = state.value.copy(
            isLoading = true
        )

        viewModelScope.launch(Dispatchers.Default) {
            val isActiveSubscribe = subscribeCacheManager.isActiveSubscribe()
            if (isActiveSubscribe.not()) {
                mutableState.value = state.value.copy(
                    isLoading = false,
                    isConfirmed = true,
                    isActiveSubscribe = false
                )
                return@launch
            }

            val newWords = state.value.words.map {
                async {
                    getByteContent(it)
                }
            }.awaitAll()

            mutableState.value = state.value.copy(
                isConfirmed = true,
                isActiveSubscribe = true,
                isLoading = false,
                words = newWords
            )
        }
    }

    private suspend fun getByteContent(word: ExerciseWordDetails) : ExerciseWordDetails{
        val imageContent = if (word.imageLink.isNullOrBlank()) null else byteContentManager.downloadByteContent(word.imageLink)
        val soundContent = if (word.soundLink.isNullOrBlank()) null else byteContentManager.downloadByteContent(word.soundLink)
        return word.copy(
            imageContent = imageContent,
            soundContent = soundContent
        )
    }

}



