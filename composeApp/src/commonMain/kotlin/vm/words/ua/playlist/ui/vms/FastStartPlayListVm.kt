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
            if (waitPlayLists(yourPlayListWaiter, publicPlayListWaiter)) {
                return@launch
            }
            val disabledTypes = HashSet<PlayListType>()
            if (yourPlayListWaiter.await().isEmpty()) {
                disabledTypes.add(PlayListType.YOUR)
            }
            if (publicPlayListWaiter.await().isEmpty()) {
                disabledTypes.add(PlayListType.PUBLIC)
            }

            mutableState.update { state ->
                state.copy(
                    type = yourPlayListWaiter.await().isNotEmpty()
                        .let { if (it) PlayListType.YOUR else PlayListType.PUBLIC },
                    disabledTypes = disabledTypes,
                    playListByType = mapOf(
                        PlayListType.YOUR to yourPlayListWaiter.await().content,
                        PlayListType.PUBLIC to publicPlayListWaiter.await().content
                    ),
                    hasNextByType = mapOf(
                        PlayListType.YOUR to yourPlayListWaiter.await().page.isLast.not(),
                        PlayListType.PUBLIC to publicPlayListWaiter.await().page.isLast.not()
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
        val mode = state.value.type

        viewModelScope.launch(Dispatchers.Default) {
            val playListModels = if (mode == PlayListType.PUBLIC) {
                playListManager.findBy(PublicPlayListFilter(ids = setOf(action.playListId), size = 1))
            } else {
                playListManager.findBy(PlayListFilter(ids = listOf(action.playListId), size = 1))
            }
            if (playListModels.isEmpty()) {
                mutableState.update {
                    it.copy(
                        isLoadingByPlayListId = it.isLoadingByPlayListId + (action.playListId to false),
                        errorMessage = ErrorMessage("PlayList not found")
                    )
                }
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

    private fun handleLoadMore() {
        when (state.value.type) {
            PlayListType.YOUR -> handleLoadYourPlayLists()
            PlayListType.PUBLIC -> handleLoadPublicPlayLists()
        }

    }

    private fun handleLoadYourPlayLists() {
        val page = state.value.yourFilter.page + 1
        val filter = state.value.yourFilter.copy(page = page)
        mutableState.update { it.copy(yourFilter = filter) }
        viewModelScope.launch(Dispatchers.Default) {
            val models = playListManager.countBy(filter) as PagedModels<PlayListCountable>
            val result = state.value.playLists() + models.content
            mutableState.update {
                it.copy(
                    playListByType = it.playListByType + (PlayListType.YOUR to result),
                    hasNextByType = it.hasNextByType + (PlayListType.YOUR to (models.page.isLast.not()))
                )
            }
        }.setErrorListener()
    }


    private fun handleLoadPublicPlayLists() {
        val page = state.value.publicFilter.page + 1
        val filter = state.value.publicFilter.copy(page = page)
        mutableState.update { it.copy(publicFilter = filter) }
        viewModelScope.launch(Dispatchers.Default) {
            val models = playListManager.countBy(filter) as PagedModels<PlayListCountable>
            val result = state.value.playLists() + models.content
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
        mutableState.update { it.copy(type = action.type) }
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