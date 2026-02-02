package vm.words.ua.playlist.ui.actions

sealed interface PublicPlayListDetailsAction {
    data class Init(val playListId: String) : PublicPlayListDetailsAction
}

