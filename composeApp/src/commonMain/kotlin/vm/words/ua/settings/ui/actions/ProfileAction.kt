package vm.words.ua.settings.ui.actions

import vm.words.ua.settings.ui.states.ProfileDialog

sealed interface ProfileAction {
    data class OpenDialog(val dialog: ProfileDialog) : ProfileAction
    data object DismissDialog : ProfileAction
    data object DismissError : ProfileAction
    data object DismissMessage : ProfileAction

    data class SaveName(val firstName: String, val lastName: String) : ProfileAction

    /** [oldPassword] пустой, если пароля нет или он забыт: тогда нужно подтверждение владения */
    data class SetPassword(val newPassword: String, val oldPassword: String?) : ProfileAction

    data class DeleteAccount(val password: String?, val reason: String?) : ProfileAction

    /** Привязать Google к текущему аккаунту */
    data object LinkGoogle : ProfileAction

    /**
     * Привязать телефон и Telegram к текущему аккаунту. Бот присылает сообщение с Yes и No,
     * приложение само ждёт подтверждения. Если бот не знает номер, пользователю нужно открыть бота.
     */
    data class LinkTelegram(val phoneNumber: String) : ProfileAction

    /** Подтвердить владение через Google (свежий idToken) */
    data object ProveWithGoogle : ProfileAction

    /**
     * Подтвердить владение через Telegram: бот присылает сообщение, приложение само ждёт Yes.
     * Для смены пароля передайте введённый [newPassword]: после подтверждения он отправится сам.
     */
    data class ProveWithTelegram(val newPassword: String? = null) : ProfileAction
}
