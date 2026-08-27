package vm.words.ua.playlist.ui.states

import vm.words.ua.playlist.domain.models.PlayListCount
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter

data class PlayListState(
    val pinnedPlayList: Item = Item(),
    val otherPlayList: Item = Item(),
    val filter: PlayListCountFilter = PlayListCountFilter(),
    val error: String? = null,
    var canUsePin: Boolean = true
) {

    data class Item(
        val content: List<PlayListCount> = emptyList(),
        val isLoading: Boolean = false,
        val currentPage: Int = 0,
        val hasMore: Boolean = true,
        val totalElements: Long = 0
    )
}

