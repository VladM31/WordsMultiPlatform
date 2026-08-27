package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.usecases.PinPlayListUseCase
import vm.words.ua.playlist.domain.usecases.UpPinPlayListUseCase
import vm.words.ua.playlist.ui.actions.PlayListAction
import vm.words.ua.playlist.ui.states.PlayListState

class PlayListViewModel(
    private val playListManager: PlayListManager,
    private val pinUseCase: PinPlayListUseCase,
    private val unPinUseCase: UpPinPlayListUseCase
) : ViewModel() {

    private val mutableState = MutableStateFlow(PlayListState())
    val state: StateFlow<PlayListState> = mutableState

    private var pinnedLoadJob: Job? = null
    private var otherLoadJob: Job? = null

    init {
        reFetch()
    }

    fun sent(action: PlayListAction) {
        when (action) {
            is PlayListAction.ReFetch -> handleReFetch()
            is PlayListAction.UpdateFilter -> handleUpdateFilter(action)
            is PlayListAction.LoadMore -> handleLoadMore(action)
            is PlayListAction.Create -> handleCreate(action.name)
            is PlayListAction.Pin -> handlePin(action.playListId)
            is PlayListAction.UnPin -> handleUnPin(action.playListId)
        }
    }

    private fun handlePin(playListId: String) {
        if (state.value.canUsePin.not()) {
            return
        }
        val item = state.value.otherPlayList.content.firstOrNull { it.id == playListId } ?: return

        viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = state.value.copy(canUsePin = false)
            val result = pinUseCase.execute(playListId)
            if (result.isSuccess.not()) {
                mutableState.value = state.value.copy(error = result.errorMessage, canUsePin = true)
                return@launch
            }
            mutableState.value = state.value.copy(
                otherPlayList = state.value.otherPlayList.copy(
                    content = state.value.otherPlayList.content.filter { it.id != playListId },
                    totalElements = (state.value.otherPlayList.totalElements - 1).coerceAtLeast(0)
                ),
                pinnedPlayList = state.value.pinnedPlayList.copy(
                    content = listOf(item) + state.value.pinnedPlayList.content,
                    totalElements = state.value.pinnedPlayList.totalElements + 1
                )
            )
            refreshSection(
                hasPin = true,
                getItem = { mutableState.value.pinnedPlayList },
                toLoadingState = { section -> mutableState.value.copy(pinnedPlayList = section) }
            ).invokeOnCompletion {
                mutableState.value = state.value.copy(canUsePin = true)
            }
        }
    }

    private fun handleUnPin(playListId: String) {
        if (state.value.canUsePin.not()) {
            return
        }
        val item = state.value.pinnedPlayList.content.firstOrNull { it.id == playListId } ?: return

        viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = state.value.copy(canUsePin = false)
            val result = unPinUseCase.execute(playListId)
            if (result.isSuccess.not()) {
                mutableState.value = state.value.copy(error = result.errorMessage, canUsePin = true)
                return@launch
            }
            mutableState.value = state.value.copy(
                pinnedPlayList = state.value.pinnedPlayList.copy(
                    content = state.value.pinnedPlayList.content.filter { it.id != playListId },
                    totalElements = (state.value.pinnedPlayList.totalElements - 1).coerceAtLeast(0)
                ),
                otherPlayList = state.value.otherPlayList.copy(
                    content = listOf(item) + state.value.otherPlayList.content,
                    totalElements = state.value.otherPlayList.totalElements + 1
                )
            )
            refreshSection(
                hasPin = false,
                getItem = { mutableState.value.otherPlayList },
                toLoadingState = { section -> mutableState.value.copy(otherPlayList = section) }
            ).invokeOnCompletion {
                mutableState.value = state.value.copy(canUsePin = true)
            }
        }
    }

    private fun handleLoadMore(action: PlayListAction.LoadMore) {
        if (action.pinned) {
            loadPlaylists(
                action.pinned,
                getItem = { mutableState.value.pinnedPlayList },
                toLoadingState = { item -> mutableState.value.copy(pinnedPlayList = item) }
            )
            return
        }
        loadPlaylists(
            action.pinned,
            getItem = { mutableState.value.otherPlayList },
            toLoadingState = { item -> mutableState.value.copy(otherPlayList = item) }
        )
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
        pinnedLoadJob?.cancel()
        otherLoadJob?.cancel()
        mutableState.value = mutableState.value.copy(
            pinnedPlayList = PlayListState.Item(),
            otherPlayList = PlayListState.Item(),
        )
        reFetch()
    }

    private fun handleUpdateFilter(action: PlayListAction.UpdateFilter) {
        pinnedLoadJob?.cancel()
        otherLoadJob?.cancel()
        mutableState.value = mutableState.value.copy(
            filter = action.filter,
            otherPlayList = PlayListState.Item(),
            pinnedPlayList = PlayListState.Item()
        )
        reFetch()
    }

    private fun reFetch() {
        val job = loadPlaylists(
            true,
            getItem = { mutableState.value.pinnedPlayList },
            toLoadingState = { item -> mutableState.value.copy(pinnedPlayList = item) }
        )
        if (job == null) {
            loadPlaylists(
                false,
                getItem = { mutableState.value.otherPlayList },
                toLoadingState = { item -> mutableState.value.copy(otherPlayList = item) }
            )
            return
        }
        job.invokeOnCompletion { _ ->
            loadPlaylists(
                false,
                getItem = { mutableState.value.otherPlayList },
                toLoadingState = { item -> mutableState.value.copy(otherPlayList = item) }
            )
        }
    }

//    private fun loadPlaylists() {
//        if (mutableState.value.isLoading || !mutableState.value.hasMore) {
//            return
//        }
//
//        // Cancel previous job if it's still running
//        currentLoadJob?.cancel()
//
//        currentLoadJob = viewModelScope.launch {
//            mutableState.value = mutableState.value.copy(isLoading = true, error = null)
//
//            try {
//                val filter = mutableState.value.filter.copy(
//                    page = mutableState.value.currentPage,
//                    size = PAGE_SIZE
//                )
//
//                val result = playListManager.countBy(filter)
//
//                val playlists = if (filter.page == 0) {
//                    result.content
//                } else {
//                    mutableState.value.playlists + result.content
//                }
//
//                mutableState.value = mutableState.value.copy(
//                    playlists = playlists,
//                    isLoading = false,
//                    currentPage = mutableState.value.currentPage + 1,
//                    hasMore = result.content.size >= PAGE_SIZE
//                )
//            } catch (e: Exception) {
//                e.printStackTrace()
//                mutableState.value = mutableState.value.copy(
//                    isLoading = false,
//                    error = e.message ?: "Unknown error"
//                )
//            }
//        }
//    }

    private fun loadPlaylists(
        hasPin: Boolean,
        getItem: () -> PlayListState.Item,
        toLoadingState: (item: PlayListState.Item) -> PlayListState
    ): Job? {
        if (getItem().isLoading || !getItem().hasMore) {
            return null
        }

        // Cancel previous job for this section only, so pinned/other loads don't cancel each other
        if (hasPin) pinnedLoadJob?.cancel() else otherLoadJob?.cancel()

        val job = viewModelScope.launch {
            mutableState.value = toLoadingState(getItem().copy(isLoading = true)).copy(error = null)

            try {
                val filter = mutableState.value.filter.copy(
                    page = getItem().currentPage,
                    size = PAGE_SIZE,
                    hasPin = hasPin
                )

                val result = playListManager.countBy(filter)

                val playlists = if (filter.page == 0) {
                    result.content
                } else {
                    getItem().content + result.content
                }
                mutableState.value = toLoadingState(
                    PlayListState.Item(
                        content = playlists,
                    isLoading = false,
                        currentPage = getItem().currentPage + 1,
                        hasMore = result.content.size >= PAGE_SIZE,
                        totalElements = result.page.totalElements
                    )
                )


            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = toLoadingState(getItem().copy(isLoading = false)).copy(
                    error = e.message ?: "Unknown error"
                )
            }
        }
        if (hasPin) pinnedLoadJob = job else otherLoadJob = job
        return job
    }

    /**
     * Re-fetches page 0 of a section from the server and replaces its content once the
     * result arrives, without clearing the currently displayed content while loading.
     * Used after pin/unpin to sync order/counts with the backend without a visual flash.
     */
    private fun refreshSection(
        hasPin: Boolean,
        getItem: () -> PlayListState.Item,
        toLoadingState: (item: PlayListState.Item) -> PlayListState
    ): Job {
        if (hasPin) pinnedLoadJob?.cancel() else otherLoadJob?.cancel()

        val job = viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = toLoadingState(getItem().copy(isLoading = true)).copy(error = null)

            try {
                val filter =
                    mutableState.value.filter.copy(page = 0, size = PAGE_SIZE, hasPin = hasPin)
                val result = playListManager.countBy(filter)

                mutableState.value = toLoadingState(
                    PlayListState.Item(
                        content = result.content,
                        isLoading = false,
                        currentPage = 1,
                        hasMore = result.content.size >= PAGE_SIZE,
                        totalElements = result.page.totalElements
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = toLoadingState(getItem().copy(isLoading = false)).copy(
                    error = e.message ?: "Unknown error"
                )
            }
        }
        if (hasPin) pinnedLoadJob = job else otherLoadJob = job
        return job
    }


    companion object {
        private const val PAGE_SIZE = 20
    }
}
