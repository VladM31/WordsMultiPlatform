package vm.words.ua.playlist.ui.states

import vm.words.ua.playlist.domain.models.PlayListCount
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter

data class PlayListState(
    val playlists: List<PlayListCount> = emptyList(),
    val filter: PlayListCountFilter = PlayListCountFilter(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)

