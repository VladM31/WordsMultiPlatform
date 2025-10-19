package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.ui.actions.PlayListAction
import vm.words.ua.playlist.ui.states.PlayListState

class PlayListViewModel(
    private val playListManager: PlayListManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(PlayListState())
    val state: StateFlow<PlayListState> = mutableState

    init {
        loadPlaylists()
    }

    fun sent(action: PlayListAction) {
        when (action) {
            is PlayListAction.ReFetch -> handleReFetch()
            is PlayListAction.UpdateFilter -> handleUpdateFilter(action)
        }
    }

    private fun handleReFetch() {
        mutableState.value = mutableState.value.copy(
            playlists = emptyList(),
            currentPage = 0,
            hasMore = true
        )
        loadPlaylists()
    }

    private fun handleUpdateFilter(action: PlayListAction.UpdateFilter) {
        mutableState.value = mutableState.value.copy(
            filter = action.filter,
            playlists = emptyList(),
            currentPage = 0,
            hasMore = true
        )
        loadPlaylists()
    }

    private fun loadPlaylists() {
        if (mutableState.value.isLoading || !mutableState.value.hasMore) return

        viewModelScope.launch {
            mutableState.value = mutableState.value.copy(isLoading = true, error = null)

            try {
                val filter = mutableState.value.filter.copy(
                    page = mutableState.value.currentPage,
                    size = PAGE_SIZE
                )

                val result = playListManager.countBy(filter)

                mutableState.value = mutableState.value.copy(
                    playlists = mutableState.value.playlists + result.content,
                    isLoading = false,
                    currentPage = mutableState.value.currentPage + 1,
                    hasMore = result.content.size >= PAGE_SIZE
                )
            } catch (e: Exception) {
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun loadMore() {
        loadPlaylists()
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}

