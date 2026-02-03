package vm.words.ua.playlist.ui.actions

import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter

sealed interface ExplorePlayListsAction {
    data object ReFetch : ExplorePlayListsAction
    data class UpdateFilter(val filter: PublicPlayListCountFilter) : ExplorePlayListsAction
    data object LoadMore : ExplorePlayListsAction
    data class AssignPlayList(val playListId: String) : ExplorePlayListsAction
    data class ToggleShowCreatedDate(val show: Boolean) : ExplorePlayListsAction
}



