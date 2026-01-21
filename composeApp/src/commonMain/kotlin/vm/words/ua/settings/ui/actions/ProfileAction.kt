package vm.words.ua.settings.ui.actions

interface ProfileAction {
    data object OpenDeleteAccountDialog : ProfileAction
    data object DismissDeleteAccountDialog : ProfileAction
    data class DeleteAccount(val password: String, val reason: String?) : ProfileAction
}