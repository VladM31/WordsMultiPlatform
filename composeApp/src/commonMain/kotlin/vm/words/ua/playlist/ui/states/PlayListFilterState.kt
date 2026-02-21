package vm.words.ua.playlist.ui.states

import vm.words.ua.playlist.domain.models.enums.PlaylistSortField

data class PlayListFilterState(
    val startCount: String = "",
    val endCount: String = "",
    val name: String = "",
    val isInited: Boolean = false,
    val sortField: PlaylistSortField = PlaylistSortField.CREATED_AT,
    val asc: Boolean = true,
    val isEnd: Boolean = false
)

