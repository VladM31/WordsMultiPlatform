package vm.words.ua.playlist.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.playlist.domain.models.PublicPlayListCountDto
import vm.words.ua.playlist.domain.models.enums.PublicPlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter

data class ExplorePlayListsState(
    val playlists: List<PublicPlayListCountDto> = emptyList(),
    val assignedIds: Set<String> = emptySet(),
    val filter: PublicPlayListCountFilter = PublicPlayListCountFilter(
        sortField = PublicPlaylistSortField.CEFR,
        asc = true
    ),
    val lastAssignedPlayList: PublicPlayListCountDto? = null,
    val isLoading: Boolean = false,
    val isAssigning: Boolean = false,

    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val showCreatedDate: Boolean = true,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState

