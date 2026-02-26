package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.models.AssignPlayListsDto
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.PlayListCountable
import vm.words.ua.playlist.domain.models.enums.PlayListType
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.domain.models.filters.PublicPlayListFilter
import vm.words.ua.playlist.ui.actions.FastStartPlayListAction
import vm.words.ua.playlist.ui.states.FastStartPlayListState

class FastStartPlayListVm(
    private val playListManager: PlayListManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(FastStartPlayListState())
    val state: StateFlow<FastStartPlayListState> = mutableState


    init {
        viewModelScope.launch(Dispatchers.Default) {
            val yourPlayListWaiter = async {
                playListManager.countBy(state.value.yourFilter) as PagedModels<PlayListCountable>
            }
            val publicPlayListWaiter = async {
                playListManager.countBy(state.value.publicFilter) as PagedModels<PlayListCountable>
            }
            val randomPlayList = async {
                (playListManager.countRandom() as PlayListCountable?)?.let { listOf(it) }
                    ?: emptyList()
            }
            if (waitPlayLists(yourPlayListWaiter, publicPlayListWaiter)) {
                return@launch
            }
            val disabledTypes = HashSet<PlayListType>()
            if (yourPlayListWaiter.await().isEmpty()) {
                disabledTypes.add(PlayListType.YOUR)
                disabledTypes.add(PlayListType.RANDOM)
            }
            if (publicPlayListWaiter.await().isEmpty()) {
                disabledTypes.add(PlayListType.PUBLIC)
            }

            mutableState.update { state ->
                state.copy(
                    visibility = yourPlayListWaiter.await().isNotEmpty()
                        .let { if (it) PlayListType.YOUR else PlayListType.PUBLIC },
                    disabledTypes = disabledTypes,
                    playListByType = mapOf(
                        PlayListType.YOUR to yourPlayListWaiter.await().content,
                        PlayListType.PUBLIC to publicPlayListWaiter.await().content,
                        PlayListType.RANDOM to randomPlayList.await()
                    ),
                    hasNextByType = mapOf(
                        PlayListType.YOUR to yourPlayListWaiter.await().page.isLast.not(),
                        PlayListType.PUBLIC to publicPlayListWaiter.await().page.isLast.not(),
                        PlayListType.RANDOM to false
                    ),
                    isLoading = false
                )
            }
        }.setErrorListener()
    }

    private suspend fun waitPlayLists(
        yourPlayListWaiter: Deferred<PagedModels<PlayListCountable>>,
        publicPlayListWaiter: Deferred<PagedModels<PlayListCountable>>
    ): Boolean {
        try {
            listOf(
                yourPlayListWaiter,
                publicPlayListWaiter
            ).awaitAll()
            return false;
        } catch (e: Exception) {
            e.printStackTrace()
            mutableState.update {
                it.copy(
                    isLoading = false, errorMessage = ErrorMessage(
                        e.message ?: "Unknown error"
                    )
                )
            }
            return true
        }
    }

    fun send(action: FastStartPlayListAction) {
        when (action) {
            is FastStartPlayListAction.LoadMore -> handleLoadMore()
            is FastStartPlayListAction.ChangeType -> handleChangeType(action)
            is FastStartPlayListAction.ToggleExpand -> handleToggleExpand(action)
            is FastStartPlayListAction.Start -> handleStart(action)
            is FastStartPlayListAction.UpdatePlayListFilter -> handleUpdatePlayListFilter(action)
            is FastStartPlayListAction.UpdatePublicPLFilter -> handleUpdatePublicPLFilter(action)
            is FastStartPlayListAction.ReloadRandom -> handleReloadRandom()
        }
    }

    private fun handleReloadRandom() {
        mutableState.update {
            it.copy(isLoadingByType = it.isLoadingByType + (PlayListType.RANDOM to true))
        }
        viewModelScope.launch(Dispatchers.Default) {
            val randomPlayList = (playListManager.countRandom() as PlayListCountable?)?.let { listOf(it) }
                ?: emptyList()
            mutableState.update {
                it.copy(
                    playListByType = it.playListByType + (PlayListType.RANDOM to randomPlayList),
                    visibility = PlayListType.RANDOM,
                    isLoadingByType = it.isLoadingByType + (PlayListType.RANDOM to false)
                )
            }
        }.setErrorListener()
    }

    private fun handleUpdatePublicPLFilter(action: FastStartPlayListAction.UpdatePublicPLFilter) {
        mutableState.update {
            it.copy(publicFilter = action.filter.copy(page = 0))
        }
        handleLoadPublicPlayLists(true)
    }

    private fun handleUpdatePlayListFilter(action: FastStartPlayListAction.UpdatePlayListFilter) {
        mutableState.update {
            it.copy(yourFilter = action.filter.copy(page = 0))
        }
        handleLoadYourPlayLists(true)
    }

    private fun handleStart(action: FastStartPlayListAction.Start) {
        val words = state.value.words(action.playListId)
        if (words.isNotEmpty() && state.value.visibility == PlayListType.YOUR) {
            mutableState.update {
                it.copy(selectedPlayListId = action.playListId)
            }
            return
        }
        val type = state.value.visibility
        mutableState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.Default) {
            var playListId: String = action.playListId
            if (type == PlayListType.PUBLIC) {
                playListId = playListManager.assignPlayLists(AssignPlayListsDto(playListIds = setOf(action.playListId)))
                    .first()
                    .id
            }

            val playListModels = getWords(PlayListType.YOUR, playListId)
            if (playListModels.isEmpty()) {
                handleEmptyWords(playListId)
                return@launch
            }
            val playList = playListModels.first()
            mutableState.update {
                it.copy(
                    selectedPlayListId = playListId,
                    wordsByPlayListId = it.wordsByPlayListId + (playListId to playList.words),
                    isLoadingByPlayListId = it.isLoadingByPlayListId + (playListId to false),
                    isExpandedByPlayListId = it.isExpandedByPlayListId + (playListId to false)
                )
            }
        }


    }

    private fun handleToggleExpand(action: FastStartPlayListAction.ToggleExpand) {
        val words = state.value.words(action.playListId)
        val currentExpanded = (state.value.isExpandedByPlayListId[action.playListId] ?: false).not()
        mutableState.update {
            it.copy(
                isExpandedByPlayListId = it.isExpandedByPlayListId + (action.playListId to (currentExpanded)),
                isLoadingByPlayListId = it.isLoadingByPlayListId + (action.playListId to (words.isEmpty()))
            )
        }
        if (words.isNotEmpty()) {
            return
        }
        if (currentExpanded.not()) {
            return
        }
        val mode = state.value.visibility

        viewModelScope.launch(Dispatchers.Default) {
            val playListModels = getWords(mode, action.playListId)
            if (playListModels.isEmpty()) {
                handleEmptyWords(action.playListId)
                return@launch
            }
            val playList = playListModels.first()

            mutableState.update {
                it.copy(
                    wordsByPlayListId = it.wordsByPlayListId + (action.playListId to playList.words),
                    isLoadingByPlayListId = it.isLoadingByPlayListId + (action.playListId to false)
                )
            }
        }.setErrorListener()
    }

    private fun handleEmptyWords(playListId: String) {
        mutableState.update {
            it.copy(
                isLoadingByPlayListId = it.isLoadingByPlayListId + (playListId to false),
                errorMessage = ErrorMessage("PlayList not found")
            )
        }
    }

    private suspend fun getWords(
        mode: PlayListType,
        playListId: String
    ): PagedModels<PlayList> {
        val playListModels = if (mode == PlayListType.PUBLIC) {
            playListManager.findBy(PublicPlayListFilter(ids = setOf(playListId), size = 1))
        } else {
            playListManager.findBy(PlayListFilter(ids = listOf(playListId), size = 1))
        }
        return playListModels
    }

    private fun handleLoadMore() {
        when (state.value.visibility) {
            PlayListType.YOUR -> handleLoadYourPlayLists()
            PlayListType.PUBLIC -> handleLoadPublicPlayLists()
            else -> {}
        }

    }

    private fun handleLoadYourPlayLists(refetch: Boolean = false) {
        val page = state.value.yourFilter.page + if (refetch) 0 else 1
        val filter = state.value.yourFilter.copy(page = page)
        mutableState.update { it.copy(yourFilter = filter) }
        viewModelScope.launch(Dispatchers.Default) {
            val previousPlayLists = if (refetch) emptyList() else state.value.playLists()
            val models = playListManager.countBy(filter) as PagedModels<PlayListCountable>
            val result = previousPlayLists + models.content
            mutableState.update {
                it.copy(
                    playListByType = it.playListByType + (PlayListType.YOUR to result),
                    hasNextByType = it.hasNextByType + (PlayListType.YOUR to (models.page.isLast.not()))
                )
            }
        }.setErrorListener()
    }


    private fun handleLoadPublicPlayLists(refetch: Boolean = false) {
        val page = state.value.publicFilter.page + if (refetch) 0 else 1
        val filter = state.value.publicFilter.copy(page = page)
        mutableState.update { it.copy(publicFilter = filter) }
        viewModelScope.launch(Dispatchers.Default) {
            val previousPlayLists = if (refetch) emptyList() else state.value.playLists()
            val models = playListManager.countBy(filter) as PagedModels<PlayListCountable>
            val result = previousPlayLists + models.content
            mutableState.update {
                it.copy(
                    playListByType = it.playListByType + (PlayListType.PUBLIC to result),
                    hasNextByType = it.hasNextByType + (PlayListType.PUBLIC to (models.page.isLast.not()))
                )
            }
        }.setErrorListener()
    }

    private fun handleChangeType(action: FastStartPlayListAction.ChangeType) {
        if (state.value.disabledTypes.contains(action.type)) {
            return
        }
        mutableState.update { it.copy(visibility = action.type) }
    }

    private fun Job.setErrorListener() {
        invokeOnCompletion { error ->
            error?.let { ex ->
                mutableState.update {
                    it.copy(
                        errorMessage = ErrorMessage(ex.message ?: "Unknown error")
                    )
                }
            }
        }
    }
}