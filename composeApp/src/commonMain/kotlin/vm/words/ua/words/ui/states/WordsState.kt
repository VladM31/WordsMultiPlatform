package vm.words.ua.words.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.domain.models.filters.WordFilter

data class WordsState(
    val selectedWords: Set<String> = emptySet(),
    val filter: WordFilter = WordFilter(),
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = true,
    val error: ErrorMessage? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)