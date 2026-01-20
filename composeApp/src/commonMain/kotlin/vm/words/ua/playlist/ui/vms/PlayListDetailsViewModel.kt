package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.playlist.domain.managers.PinPlayListManager
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.models.PinPlayList
import vm.words.ua.playlist.domain.models.UpdatePlayList
import vm.words.ua.playlist.domain.models.filters.DeletePlayListFilter
import vm.words.ua.playlist.domain.models.filters.PlayListFilter
import vm.words.ua.playlist.ui.actions.PlayListDetailsAction
import vm.words.ua.playlist.ui.states.PlayListDetailsState

class PlayListDetailsViewModel(
    private val playListManager: PlayListManager,
    private val pinPlayListManager: PinPlayListManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(PlayListDetailsState())
    val state: StateFlow<PlayListDetailsState> = mutableState

    fun sent(action: PlayListDetailsAction) {
        when (action) {
            is PlayListDetailsAction.Fetch -> fetchPlayList(action.id)
            is PlayListDetailsAction.SelectWord -> selectWord(action.id, action.position)
            is PlayListDetailsAction.UnSelect -> unSelect()
            is PlayListDetailsAction.UnPin -> unPin()
            is PlayListDetailsAction.HandleEdit -> handleEdit(action.name)
            is PlayListDetailsAction.Delete -> handleDelete()
            is PlayListDetailsAction.ReFetch -> fetchPlayList(state.value.id)
        }
    }

    private fun handleDelete() {
        viewModelScope.launch {
            try {
                playListManager.delete(DeletePlayListFilter(listOf(state.value.id)))
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mutableState.value = mutableState.value.copy(
                isEnd = true
            )
        }
    }

    private fun handleEdit(name: String) {
        viewModelScope.launch {
            try {
                playListManager.update(
                    listOf(
                        UpdatePlayList(
                            id = state.value.id,
                            name = name
                        )
                    )
                )

                mutableState.value = mutableState.value.copy(
                    name = name
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun unPin() {
        if (state.value.selectedWords.isEmpty()) return

        viewModelScope.launch(Dispatchers.Default) {
            val unPinWords = state.value.selectedWords.keys.map {
                PinPlayList(
                    wordId = it,
                    playListId = state.value.id
                )
            }
            pinPlayListManager.unpin(unPinWords)

            val newWords = state.value.words.filter {
                state.value.selectedWords.containsKey(it.userWord.id).not()
            }

            mutableState.value = mutableState.value.copy(
                words = newWords,
                selectedWords = emptyMap()
            )
        }
    }

    private fun unSelect() {
        mutableState.value = mutableState.value.copy(
            selectedWords = emptyMap()
        )
    }

    private fun selectWord(id: String, position: Int) {
        val newSelectedWords = if (state.value.selectedWords.containsKey(id)) {
            state.value.selectedWords - id
        } else {
            state.value.selectedWords + (id to position)
        }

        mutableState.value = mutableState.value.copy(
            selectedWords = newSelectedWords
        )
    }

    private fun fetchPlayList(id: String) {
        if (id.isEmpty()) return

        viewModelScope.launch {
            try {
                val list = playListManager.findBy(PlayListFilter(ids = listOf(id)))

                list.content.firstOrNull()?.let {
                    mutableState.value = mutableState.value.copy(
                        name = it.name,
                        words = it.words,
                        id = it.id
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
