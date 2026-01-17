package vm.words.ua.learning.ui.states

import vm.words.ua.learning.domain.models.LearningHistory

data class LearningHistoryListState(
    val history: List<LearningHistory> = emptyList(),
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val error: String? = null
)

