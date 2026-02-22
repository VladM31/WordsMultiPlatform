package vm.words.ua.exercise.ui.states

import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.ui.utils.isEnableImage

data class WriteByImageAndFieldState(
    override val words: List<ExerciseWordDetails> = emptyList(),
    val wordIndex: Int = 0,
    val isConfirm: Boolean? = null,
    val wordText: String? = null,
    val isEditEnable: Boolean = true,
    override val transactionId: String = "",
    override val isEnd: Boolean = false,
    val isInited: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    val attempts: Int = 0,
    override val grades: List<Int> = emptyList(),
    override val exercise: Exercise = Exercise.WORD_BY_WRITE_TRANSLATE
) : ExerciseState {
    fun currentWord() = words[wordIndex]

    fun enableImage(): Boolean {
        return isActiveSubscribe && currentWord().imageContent != null && exercise.isEnableImage()
    }
}
