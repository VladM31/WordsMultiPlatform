package vm.words.ua.playlist.ui.states

import vm.words.ua.playlist.domain.models.PlayList

data class PlayListDetailsState(
    val name: String = "Loading...",
    val id: String = "",
    val selectedWords: Map<String, Int> = emptyMap(),
    val words: List<PlayList.PinnedWord> = emptyList(),
    val isEnd: Boolean = false
)

