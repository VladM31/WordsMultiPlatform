package vm.words.ua.playlist.ui.states

data class PlayListFilterState(
    val startCount: String = "",
    val endCount: String = "",
    val name: String = "",
    val isInited: Boolean = false,
    val isEnd: Boolean = false
)

