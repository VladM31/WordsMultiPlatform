package vm.words.ua.playlist.ui.actions

import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter

sealed interface PlayListAction {
    data object ReFetch : PlayListAction
    data class LoadMore(val pinned: Boolean) : PlayListAction
    data class UpdateFilter(val filter: PlayListCountFilter) : PlayListAction
    data class Create(val name: String) : PlayListAction

    data class Pin(val playListId: String) : PlayListAction
    data class UnPin(val playListId: String) : PlayListAction
}
