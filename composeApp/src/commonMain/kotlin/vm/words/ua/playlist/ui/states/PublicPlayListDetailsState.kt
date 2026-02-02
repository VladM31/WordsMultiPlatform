package vm.words.ua.playlist.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.playlist.domain.models.PlayList

data class PublicPlayListDetailsState(
    val playList: PlayList? = null,
    val isLoading: Boolean = true,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState

