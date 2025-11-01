package vm.words.ua.exercise.ui.states

import androidx.compose.runtime.Immutable
import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.data.MatchWordsBox

@Immutable
data class MatchWordsState(
    val originalWords: List<MatchWordsBox> = emptyList(),
    val translateWords: List<MatchWordsBox> = emptyList(),
    val words: List<ExerciseWordDetails> = emptyList(),
    val isInited: Boolean = false,
    val attempts: Int = 0,
    override val isEnd: Boolean = false,
    val original: WordBox? = null,
    val translate: WordBox? = null,
    val transactionId: String = "",
) : EndetableState {
    data class WordBox(
        val id: String,
        val index: Int,
        val original: String,
        val translate: String
    )
}
