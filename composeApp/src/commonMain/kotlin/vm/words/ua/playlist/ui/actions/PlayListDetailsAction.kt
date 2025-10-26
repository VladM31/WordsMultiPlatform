package vm.words.ua.playlist.ui.actions

sealed class PlayListDetailsAction {
    data class Fetch(val id: String) : PlayListDetailsAction()
    data class SelectWord(val id: String, val position: Int) : PlayListDetailsAction()
    data object UnSelect : PlayListDetailsAction()
    data object UnPin : PlayListDetailsAction()
    data class HandleEdit(val name: String) : PlayListDetailsAction()
    data object Delete : PlayListDetailsAction()
    data object ReFetch : PlayListDetailsAction()
}

