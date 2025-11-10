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

    private var currentLoadJob: kotlinx.coroutines.Job? = null

    init {
        loadPlaylists()
    }

    fun sent(action: PlayListAction) {
        when (action) {
            is PlayListAction.ReFetch -> handleReFetch()
            is PlayListAction.UpdateFilter -> handleUpdateFilter(action)
            PlayListAction.LoadMore -> loadPlaylists()
            is PlayListAction.Create -> handleCreate(action.name)
        }
    }

    private fun handleCreate(name: String) {
        viewModelScope.launch {
            try {
                playListManager.save(listOf(vm.words.ua.playlist.domain.models.SavePlayList(name)))
                handleReFetch()
            } catch (e: Exception) {
                mutableState.value =
                    mutableState.value.copy(error = e.message ?: "Failed to create playlist")
            }
        }
    }

    private fun handleReFetch() {
        currentLoadJob?.cancel()
        mutableState.value = mutableState.value.copy(
            playlists = emptyList(),
            currentPage = 0,
            hasMore = true,
            isLoading = false
        )
        loadPlaylists()
    }

    private fun handleUpdateFilter(action: PlayListAction.UpdateFilter) {
        currentLoadJob?.cancel()
        mutableState.value = mutableState.value.copy(
            filter = action.filter,
            playlists = emptyList(),
            currentPage = 0,
            hasMore = true,
            isLoading = false
        )
        loadPlaylists()
    }

    private fun loadPlaylists() {
        if (mutableState.value.isLoading || !mutableState.value.hasMore) {
            return
        }

        // Cancel previous job if it's still running
        currentLoadJob?.cancel()

        currentLoadJob = viewModelScope.launch {
            mutableState.value = mutableState.value.copy(isLoading = true, error = null)

            try {
                val filter = mutableState.value.filter.copy(
                    page = mutableState.value.currentPage,
                    size = PAGE_SIZE
                )

                val result = playListManager.countBy(filter)

                val playlists = if (filter.page == 0) {
                    result.content
                } else {
                    mutableState.value.playlists + result.content
                }

                mutableState.value = mutableState.value.copy(
                    playlists = playlists,
                    isLoading = false,
                    currentPage = mutableState.value.currentPage + 1,
                    hasMore = result.content.size >= PAGE_SIZE
                )
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }


    companion object {
        private const val PAGE_SIZE = 20
    }
}
