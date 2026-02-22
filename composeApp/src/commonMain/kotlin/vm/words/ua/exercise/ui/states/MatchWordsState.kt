package vm.words.ua.exercise.ui.states

import androidx.compose.runtime.Immutable
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.data.MatchWordsBox
import vm.words.ua.exercise.domain.models.enums.Exercise

@Immutable
data class MatchWordsState(
    val originalWords: List<MatchWordsBox> = emptyList(),
    val translateWords: List<MatchWordsBox> = emptyList(),
    override val words: List<ExerciseWordDetails> = emptyList(),
    val isInited: Boolean = false,
    val attempts: Int = 0,
    override val isEnd: Boolean = false,
    override val grades: List<Int> = emptyList(),
    val original: WordBox? = null,
    val translate: WordBox? = null,
    override val transactionId: String = "",
    override val exercise: Exercise = Exercise.MATCH_WORDS,
) : ExerciseState {
    override val wordIndex: Int get() = grades.size

    data class WordBox(
        val id: String,
        val index: Int,
        val original: String,
        val translate: String
    )
}
