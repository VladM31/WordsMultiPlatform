package vm.words.ua.playlist.ui.actions

import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter

sealed interface PlayListAction {
    data object ReFetch : PlayListAction
    data object LoadMore : PlayListAction
    data class UpdateFilter(val filter: PlayListCountFilter) : PlayListAction
    data class Create(val name: String) : PlayListAction
}
