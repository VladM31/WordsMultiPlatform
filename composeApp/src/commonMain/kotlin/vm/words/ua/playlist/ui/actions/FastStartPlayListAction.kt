package vm.words.ua.playlist.ui.actions

import vm.words.ua.playlist.domain.models.enums.PlayListType

interface FastStartPlayListAction {

    data class ChangeType(val type: PlayListType) : FastStartPlayListAction
    data object LoadMore : FastStartPlayListAction
}