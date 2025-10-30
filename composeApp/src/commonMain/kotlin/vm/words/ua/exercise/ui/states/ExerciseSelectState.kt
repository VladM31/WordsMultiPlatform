package vm.words.ua.exercise.ui.states

import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.domain.models.data.ExerciseSelection
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.subscribes.domain.managers.SubscribeCacheManager
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ExerciseSelectState @OptIn(ExperimentalUuidApi::class) constructor(
    val exercises: List<ExerciseSelection> = Exercise.entries.map {
        ExerciseSelection(it)
    },
    val selectedExercises: Map<Exercise, Int> = mapOf(),
    var number: Int = 0,
    val transactionId: String = Uuid.random().toString(),
    val isConfirmed: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    val isLoading: Boolean = false,

    val isInited: Boolean = false,
    val words: List<ExerciseWordDetails> = emptyList()
)

