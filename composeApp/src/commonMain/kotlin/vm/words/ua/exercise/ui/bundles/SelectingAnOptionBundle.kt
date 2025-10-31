package vm.words.ua.exercise.ui.bundles

import vm.words.ua.exercise.domain.models.data.ExerciseSelection
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails

data class  SelectingAnOptionBundle(
    val number: Int = 0,
    val exercises: List<ExerciseSelection> = emptyList(),
    val words: List<ExerciseWordDetails> = emptyList(),

    val transactionId: String = "",
    val isActiveSubscribe: Boolean = false,
) {
}