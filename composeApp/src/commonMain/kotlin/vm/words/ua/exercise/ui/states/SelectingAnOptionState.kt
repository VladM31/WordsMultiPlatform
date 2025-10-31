package vm.words.ua.exercise.ui.states

import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise
import androidx.compose.runtime.Immutable
import vm.words.ua.exercise.ui.utils.isEnableImage

@Immutable
data class SelectingAnOptionState(
    val wordIndex: Int = 0,
    val words: List<ExerciseWordDetails> = emptyList(),
    val wordsToChoose: List<ExerciseWordDetails> = emptyList(),
    val isInited: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    override val isEnd: Boolean = false,
    val waitNext: Boolean = false,
    val isCorrect: Boolean? = null,
    val grades: List<Int> = emptyList(),
    val exercise: Exercise = Exercise.WORD_BY_ORIGINALS,
    val transactionId: String = "",
    val isSoundBeforeAnswer: Boolean = false,
    val isSoundAfterAnswer: Boolean = false
) : EndetableState{
    fun currentWord() = words[wordIndex]

    fun enableImage() : Boolean {
        return isActiveSubscribe && currentWord().imageContent != null && exercise.isEnableImage()
    }
}
