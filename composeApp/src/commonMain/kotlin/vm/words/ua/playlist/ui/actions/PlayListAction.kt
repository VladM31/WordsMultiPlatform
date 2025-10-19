package vm.words.ua.playlist.ui.actions

import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter

sealed class PlayListAction {
    data object ReFetch : PlayListAction()
    data class UpdateFilter(val filter: PlayListCountFilter) : PlayListAction()
}

