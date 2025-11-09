package vm.words.ua.words.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.words.domain.managers.WordManager
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.domain.models.filters.WordFilter
import vm.words.ua.words.ui.actions.WordsAction
import vm.words.ua.words.ui.states.WordsState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class WordsViewModel(
    private val wordManager: WordManager,
    private val userCacheManager: UserCacheManager
) : ViewModel() {


    private val mutableState: MutableStateFlow<WordsState> = MutableStateFlow(
        WordsState(
            filter = WordFilter(
                userId = WordFilter.UserId(id = userCacheManager.user.id, isIn = false)
            ),
        )
    )
    val state: StateFlow<WordsState> = mutableState

    init {
        loadMore(0)
    }

    fun sent(action: WordsAction) {
        when (action) {
            is WordsAction.SelectWord -> selectWord(action.word)
            is WordsAction.Clear -> clear()
            is WordsAction.LoadMore -> loadMore()
            is WordsAction.UpdateFilter -> updateFilter(action)
        }
    }

    private fun loadMore() {
        loadMore(state.value.currentPage + 1)
    }

    private fun loadMore(newPage: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            if (state.value.hasMore.not()) {
                return@launch
            }
            mutableState.apply {
                value = value.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val paged = wordManager.findBy(state.value.filter.copy(page = newPage))
                val newWords = if (newPage == 0) {
                    paged.content
                } else {
                    state.value.words + paged.content
                }
                mutableState.apply {
                    value = value.copy(
                        currentPage = newPage,
                        hasMore = paged.page.totalPages - 1 >= newPage,
                        words = newWords,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                mutableState.apply {
                    value = value.copy(
                        isLoading = false,
                        error = ErrorMessage(e.message.orEmpty())
                    )
                }
            }
        }

    }

    @OptIn(ExperimentalTime::class)
    private fun updateFilter(action: WordsAction.UpdateFilter) {
        if (state.value.filter == action.filter) {
            return
        }
        mutableState.value = state.value.copy(
            filter = action.filter,
            currentPage = 0,
            hasMore = true,
            words = listOf(),
            isLoading = true,
            filterId = Clock.System.now().toEpochMilliseconds()
        )
        loadMore(0)
    }


    private fun selectWord(word: Word) {
        val selectedWords = if (state.value.selectedWords.contains(word.id)) {
            state.value.selectedWords - word.id
        } else {
            state.value.selectedWords + (word.id to word)
        }


        mutableState.apply {
            value = value.copy(
                selectedWords = selectedWords
            )
        }
    }

    private fun clear() {
        mutableState.apply {
            value = value.copy(
                selectedWords = emptyMap()
            )
        }
    }

}