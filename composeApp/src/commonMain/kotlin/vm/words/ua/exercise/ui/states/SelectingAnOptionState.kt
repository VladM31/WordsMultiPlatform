package vm.words.ua.exercise.ui.states

import androidx.compose.runtime.Immutable
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.ui.utils.isEnableImage

@Immutable
data class SelectingAnOptionState(
    override val wordIndex: Int = 0,
    override val words: List<ExerciseWordDetails> = emptyList(),
    val wordsToChoose: List<ExerciseWordDetails> = emptyList(),
    val isInited: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    override val isEnd: Boolean = false,
    val waitNext: Boolean = false,
    val isCorrect: Boolean? = null,
    override val grades: List<Int> = emptyList(),
    override val exercise: Exercise = Exercise.WORD_BY_ORIGINALS,
    override val transactionId: String = "",
    val isSoundBeforeAnswer: Boolean = false,
    val isSoundAfterAnswer: Boolean = false
) : ExerciseState{
    fun currentWord() = words[wordIndex]

    fun enableImage() : Boolean {
        return isActiveSubscribe && currentWord().imageContent != null && exercise.isEnableImage()
    }
}
