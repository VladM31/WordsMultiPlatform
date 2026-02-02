package vm.words.ua.playlist.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.models.enums.PlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PublicPlayListFilter
import vm.words.ua.playlist.ui.actions.PublicPlayListDetailsAction
import vm.words.ua.playlist.ui.states.PublicPlayListDetailsState

class PublicPlayListDetailsViewModel(
    private val playListManager: PlayListManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(PublicPlayListDetailsState(isLoading = false))
    val state: StateFlow<PublicPlayListDetailsState> = mutableState


    fun sent(action: PublicPlayListDetailsAction) {
        when (action) {
            is PublicPlayListDetailsAction.Init -> handleInit(action.playListId)
        }
    }

    private fun handleInit(id: String) {
        loadPlayList(id)
    }

    private fun loadPlayList(playListId: String) {
        if (mutableState.value.isLoading) return

        viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = mutableState.value.copy(isLoading = true, errorMessage = null)

            try {
                val filter = PublicPlayListFilter(
                    ids = setOf(playListId),
                    name = null,
                    sortField = PlaylistSortField.CREATED_AT,
                    asc = false,
                    page = 0,
                    size = 1
                )

                val result = playListManager.findBy(filter)
                val playList = result.content.firstOrNull()


                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    playList = playList,
                    errorMessage = if (playList != null) null else ErrorMessage("Playlist not found")
                )
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    errorMessage = ErrorMessage(e.message ?: "Failed to load playlist")
                )
            }
        }
    }

}

