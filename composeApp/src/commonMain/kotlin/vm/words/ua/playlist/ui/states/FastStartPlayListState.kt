package vm.words.ua.playlist.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.PlayListCountable
import vm.words.ua.playlist.domain.models.enums.PlayListType
import vm.words.ua.playlist.domain.models.enums.PublicPlaylistSortField
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter

data class FastStartPlayListState(
    val visibility: PlayListType = PlayListType.YOUR,
    val disabledTypes: Set<PlayListType> = emptySet(),
    val playListByType: Map<PlayListType, List<PlayListCountable>> = emptyMap(),
    val isLoadingByType: Map<PlayListType, Boolean> = emptyMap(),
    val hasNextByType: Map<PlayListType, Boolean> = emptyMap(),

    val wordsByPlayListId: Map<String, List<PlayList.PinnedWord>> = emptyMap(),
    val isLoadingByPlayListId: Map<String, Boolean> = emptyMap(),
    val isExpandedByPlayListId: Map<String, Boolean> = emptyMap(),

    val publicFilter: PublicPlayListCountFilter = PublicPlayListCountFilter(
        asc = true,
        sortField = PublicPlaylistSortField.NAME
    ),
    val yourFilter: PlayListCountFilter = PlayListCountFilter(),

    val selectedPlayListId: String? = null,
    val isLoading: Boolean = true,
    override val errorMessage: ErrorMessage? = null

) : ErrorableState {

    data class StartState(
        val playListId: String
    )

    fun words(playListId: String): List<PlayList.PinnedWord> {
        return wordsByPlayListId[playListId] ?: emptyList()
    }

    fun playLists(): List<PlayListCountable> {
        return playListByType[visibility] ?: emptyList()
    }

    fun isLoadingPlayListType(): Boolean {
        return isLoadingByType[visibility] ?: false
    }

    fun hasMorePlayLists(): Boolean {
        return hasNextByType[visibility] ?: false
    }

    fun isLoadingWords(playListId: String): Boolean {
        return isLoadingByPlayListId[playListId] ?: false
    }
}
