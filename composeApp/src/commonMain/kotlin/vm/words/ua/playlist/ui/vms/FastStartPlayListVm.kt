package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.models.PlayListCountable
import vm.words.ua.playlist.domain.models.enums.PlayListType
import vm.words.ua.playlist.ui.actions.FastStartPlayListAction
import vm.words.ua.playlist.ui.states.FastStartPlayListState

class FastStartPlayListVm(
    private val playListManager: PlayListManager
) : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(FastStartPlayListState())
    val state: StateFlow<FastStartPlayListState> = mutableStateFlow

    fun send(action: FastStartPlayListAction) {
        when (action) {
            is FastStartPlayListAction.LoadMore -> handleLoadMore()
            is FastStartPlayListAction.ChangeType -> handleChangeType(action)
        }
    }

    private fun handleLoadMore() {
        if (state.value.type == PlayListType.YOUR) {
            handleLoadYourPlayLists()
        }

    }

    private fun handleLoadYourPlayLists() {
        val page = state.value.yourFilter.page + 1
        val filter = state.value.yourFilter.copy(page = page)
        mutableStateFlow.update { it.copy(yourFilter = filter) }
        viewModelScope.launch(Dispatchers.Default) {
            val result = state.value.playLists() + playListManager.countBy(filter) as List<PlayListCountable>
            mutableStateFlow.update {
                it.copy(
                    playListByType = it.playListByType + (PlayListType.YOUR to result)
                )
            }
        }
    }

    private fun handleChangeType(action: FastStartPlayListAction.ChangeType) {

        if (state.value.disabledTypes.contains(action.type)) {
            return
        }
        mutableStateFlow.update { it.copy(type = action.type) }
    }
}