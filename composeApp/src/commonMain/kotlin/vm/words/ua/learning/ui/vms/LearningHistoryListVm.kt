package vm.words.ua.learning.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.learning.domain.managers.LearningHistoryManager
import vm.words.ua.learning.domain.models.LearningHistoryFilter
import vm.words.ua.learning.ui.actions.LearningHistoryListAction
import vm.words.ua.learning.ui.states.LearningHistoryListState

class LearningHistoryListVm(
    private val manager: LearningHistoryManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(LearningHistoryListState())
    val state: StateFlow<LearningHistoryListState> = mutableState

    init {
        loadMore(0)
    }

    fun sent(action: LearningHistoryListAction) {
        when (action) {
            is LearningHistoryListAction.LoadMore -> loadMore(state.value.currentPage + 1)
            is LearningHistoryListAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        mutableState.value = LearningHistoryListState()
        loadMore(0)
    }

    private fun loadMore(page: Int) {
        if (state.value.isLoading || (!state.value.hasMore && page > 0)) {
            return
        }

        mutableState.value = state.value.copy(isLoading = true, error = null)

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val filter = LearningHistoryFilter(
                    page = page,
                    size = PAGE_SIZE
                )
                val result = manager.getLearningHistory(filter)

                val newHistory = if (page == 0) {
                    result.content
                } else {
                    state.value.history + result.content
                }

                mutableState.value = state.value.copy(
                    history = newHistory,
                    currentPage = page,
                    hasMore = result.page.totalPages - 1 > page,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                mutableState.value = state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}

