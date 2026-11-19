package vm.words.ua.auth.ui.actions

sealed interface TelegramLoginAction {
    data class SetPhoneNumber(val value: String) : TelegramLoginAction
    data object Submit : TelegramLoginAction
    data object CheckLogin : TelegramLoginAction
    data object SubmitMiniApp : TelegramLoginAction

    /** Прервать ожидание подтверждения, сбросить код и вернуться к вводу номера */
    data object Cancel : TelegramLoginAction
}