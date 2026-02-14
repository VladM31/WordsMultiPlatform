package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.models.AssignPlayListsDto
import vm.words.ua.playlist.domain.models.PublicPlayListCountDto
import vm.words.ua.playlist.ui.actions.ExplorePlayListsAction
import vm.words.ua.playlist.ui.states.ExplorePlayListsState

class ExplorePlayListsViewModel(
    private val playListManager: PlayListManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(ExplorePlayListsState())
    val state: StateFlow<ExplorePlayListsState> = mutableState

    private var currentLoadJob: kotlinx.coroutines.Job? = null

    init {
        loadAssignedPlaylists()
    }

    fun sent(action: ExplorePlayListsAction) {
        when (action) {
            is ExplorePlayListsAction.ReFetch -> handleReFetch()
            is ExplorePlayListsAction.UpdateFilter -> handleUpdateFilter(action)
            is ExplorePlayListsAction.LoadMore -> loadPlaylists()
            is ExplorePlayListsAction.AssignPlayList -> handleAssignPlayList(action.playList)
            is ExplorePlayListsAction.ToggleShowCreatedDate -> handleToggleShowCreatedDate(action.show)
        }
    }

    private fun handleToggleShowCreatedDate(show: Boolean) {
        mutableState.value = mutableState.value.copy(showCreatedDate = show)
    }

    private fun loadAssignedPlaylists() {
        viewModelScope.launch(Dispatchers.Default) {
            loadPlaylists()
        }
    }

    private fun handleAssignPlayList(playList: PublicPlayListCountDto) {
        val playListId = playList.id
        viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = mutableState.value.copy(isAssigning = true)
            try {
                val currentAssigned = mutableState.value.assignedIds
                val newAssigned = currentAssigned + playListId
                playListManager.assignPlayLists(AssignPlayListsDto(newAssigned))

                // Remove the playlist from the list and update assigned ids
                val updatedPlaylists = mutableState.value.playlists.filter { it.id != playListId }
                mutableState.value = mutableState.value.copy(
                    playlists = updatedPlaylists,
                    assignedIds = newAssigned,
                    isAssigning = false,
                    filter = mutableState.value.filter.copy(notInIds = newAssigned),
                    lastAssignedPlayList = playList,
                )
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    isAssigning = false,
                    errorMessage = ErrorMessage(e.message ?: "Failed to assign playlist"),
                    lastAssignedPlayList = null
                )
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

    private fun handleUpdateFilter(action: ExplorePlayListsAction.UpdateFilter) {
        currentLoadJob?.cancel()
        mutableState.value = mutableState.value.copy(
            filter = action.filter.copy(page = 0),
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

        currentLoadJob?.cancel()

        currentLoadJob = viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = mutableState.value.copy(isLoading = true, errorMessage = null)

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

                val newPage = mutableState.value.currentPage + 1;
                mutableState.value = mutableState.value.copy(
                    playlists = playlists,
                    isLoading = false,
                    currentPage = newPage,
                    hasMore = result.page.totalPages > newPage
                )
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    errorMessage = ErrorMessage(e.message ?: "Failed to load playlists")
                )
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}

