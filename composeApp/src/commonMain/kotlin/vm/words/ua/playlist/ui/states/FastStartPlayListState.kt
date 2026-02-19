package vm.words.ua.playlist.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.PlayListCountable
import vm.words.ua.playlist.domain.models.enums.PlayListType
import vm.words.ua.playlist.domain.models.filters.PlayListCountFilter
import vm.words.ua.playlist.domain.models.filters.PublicPlayListCountFilter

data class FastStartPlayListState(
    val type: PlayListType = PlayListType.YOUR,
    val disabledTypes: Set<PlayListType> = emptySet(),
    val playListByType: Map<PlayListType, List<PlayListCountable>> = emptyMap(),

    val wordsByPlayListId: Map<String, List<PlayList.PinnedWord>> = emptyMap(),
    val isLoadingByPlayListId: Map<String, Boolean> = emptyMap(),
    val isExpandedByPlayListId: Map<String, Boolean> = emptyMap(),

    val publicFilter: PublicPlayListCountFilter = PublicPlayListCountFilter(),
    val yourFilter: PlayListCountFilter = PlayListCountFilter(),


    val isLoading: Boolean = true,
    override val errorMessage: ErrorMessage? = null

) : ErrorableState {

    fun words(playListId: String): List<PlayList.PinnedWord> {
        return wordsByPlayListId[playListId] ?: emptyList()
    }

    fun playLists(): List<PlayListCountable> {
        return playListByType[type] ?: emptyList()
    }
}
