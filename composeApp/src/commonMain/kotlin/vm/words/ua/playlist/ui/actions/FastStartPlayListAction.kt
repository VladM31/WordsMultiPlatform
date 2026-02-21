package vm.words.ua.playlist.ui.actions

import vm.words.ua.playlist.domain.models.enums.PlayListType
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter

interface FastStartPlayListAction {

    data class ChangeType(val type: PlayListType) : FastStartPlayListAction
    data class ToggleExpand(val playListId: String) : FastStartPlayListAction
    data class Start(val playListId: String) : FastStartPlayListAction

    data class UpdatePublicPLFilter(val filter: PublicPlayListCountFilter) : FastStartPlayListAction
    data class UpdatePlayListFilter(val filter: PlayListCountFilter) : FastStartPlayListAction

    data object LoadMore : FastStartPlayListAction

}