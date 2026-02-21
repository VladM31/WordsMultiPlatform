package vm.words.ua.playlist.ui.actions

import vm.words.ua.playlist.domain.models.enums.PlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter

sealed class PlayListFilterAction {
    data class Init(val filter: PlayListCountFilter) : PlayListFilterAction()
    data class ChangeStartCount(val startCount: String) : PlayListFilterAction()
    data class ChangeEndCount(val endCount: String) : PlayListFilterAction()
    data class ChangeName(val name: String) : PlayListFilterAction()

    data class SetSortField(val sortField: PlaylistSortField) : PlayListFilterAction()
    data class SetAsc(val asc: Boolean) : PlayListFilterAction()
    data object Find : PlayListFilterAction()
    data object Clear : PlayListFilterAction()
}

