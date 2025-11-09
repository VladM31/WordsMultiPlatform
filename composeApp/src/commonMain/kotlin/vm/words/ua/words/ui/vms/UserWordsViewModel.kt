package vm.words.ua.words.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.playlist.domain.managers.PinPlayListManager
import vm.words.ua.playlist.domain.models.PinPlayList
import vm.words.ua.words.domain.managers.UserWordManager
import vm.words.ua.words.ui.actions.UserWordsAction
import vm.words.ua.words.ui.states.UserWordsState

class UserWordsViewModel(
    private val userWordManager: UserWordManager,
    private val pinPlayListManager: PinPlayListManager
) : ViewModel() {

    private val mutableState: MutableStateFlow<UserWordsState> = MutableStateFlow(UserWordsState())
    val state: StateFlow<UserWordsState> = mutableState


    fun sent(action: UserWordsAction) {
        when (action) {
            is UserWordsAction.SelectWord -> handleSelectWord(action)
            is UserWordsAction.UpdateFilter -> handleChangeFilter(action)
            is UserWordsAction.Clear -> handleClear()
            is UserWordsAction.PinWords -> pinWords(action.playListId)
            is UserWordsAction.ReFetch -> handleReFetch()
            is UserWordsAction.LoadMore -> handleLoadMore()
        }
    }

    private fun handleClear() {
        mutableState.value = mutableState.value.copy(
            selectedWords = emptySet()
        )
    }



    private fun handleSelectWord(action: UserWordsAction.SelectWord) {
        if (state.value.selectedWords.contains(action.wordId)) {
            mutableState.value = mutableState.value.copy(
                selectedWords = mutableState.value.selectedWords - action.wordId
            )
            return
        }
        mutableState.value = mutableState.value.copy(
            selectedWords = mutableState.value.selectedWords + action.wordId
        )
    }

    private fun handleChangeFilter(action: UserWordsAction.UpdateFilter) {
        if (state.value.filter == action.filter) {
            return
        }

        mutableState.value = mutableState.value.copy(
            filter = action.filter,
            selectedWords = emptySet(),
            page = 0,
            isLoading = false,
            hasMore = true,
        )
        loadMore(state.value.page)
    }

    private fun pinWords(playListId: String) {
        if (state.value.selectedWords.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val pins = state.value.selectedWords
                .map {
                    PinPlayList(
                        playListId = playListId,
                        wordId = it
                    )
                }

            pinPlayListManager.pin(pins)

            mutableState.value = state.value.copy(
                selectedWords = emptySet(),
                openPlayList = UserWordsState.OpenPlayList(playListId)
            )

            mutableState.value = state.value.copy(
                openPlayList = null
            )
        }
    }


    private fun handleReFetch() {
        mutableState.value = mutableState.value.copy(
            page = 0
        )
        loadMore(state.value.page)
    }

    private fun handleLoadMore() {

        val nextPage = if (state.value.userWords.isEmpty()) 0 else state.value.page + 1
        mutableState.value = mutableState.value.copy(
            page = nextPage
        )
        loadMore(nextPage)
    }


    private fun loadMore(pageNumber: Int) {
        if (state.value.run { isLoading || hasMore.not() }) {
            return
        }

        mutableState.value = mutableState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val paged = userWordManager.findBy(
                    state.value.filter.copy(page = pageNumber)
                )

                val currentWords = if (pageNumber == 0) {
                    paged.content
                } else {
                    mutableState.value.userWords + paged.content
                }

                mutableState.value = mutableState.value.copy(
                    userWords = currentWords,
                    page = pageNumber,
                    isLoading = false,
                    hasMore = paged.page.totalPages > pageNumber
                )
            } catch (e: Exception) {
                println(e.message)
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    errorMessage = ErrorMessage(e.message.orEmpty()),
                    hasMore = false
                )
            }
        }
    }


}