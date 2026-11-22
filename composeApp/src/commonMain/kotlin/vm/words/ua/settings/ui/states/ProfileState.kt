package vm.words.ua.settings.ui.states

import vm.words.ua.auth.domain.models.OwnershipProof
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState

enum class ProfileDialog {
    NONE, EDIT_NAME, PASSWORD, DELETE, LINK_TELEGRAM
}

data class ProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String? = null,
    val phoneNumber: String? = null,
    val dialog: ProfileDialog = ProfileDialog.NONE,
    val isLoading: Boolean = false,
    /** Подтверждение владения для смены пароля или удаления (Google или Telegram) */
    val proof: OwnershipProof? = null,
    /** Код, отправленный боту: ждём, пока пользователь нажмёт Yes */
    val telegramProofCode: String? = null,
    /** Код привязки Telegram: ждём, пока пользователь подтвердит в боте */
    val telegramLinkCode: String? = null,
    /** Бот не знает номер: пользователь должен открыть бота, отправить контакт и подтвердить в сообщении */
    val botActivationRequired: Boolean = false,
    /** Задан ли пароль: `false` скрывает поля текущего пароля, `null` пока неизвестно */
    val hasPassword: Boolean? = null,
    val isGoogleAvailable: Boolean = false,
    /** Сообщение об успешном действии */
    val message: String? = null,
    override val errorMessage: ErrorMessage? = null,
) : ErrorableState {

    /**
     * Привязать Google можно, пока у аккаунта нет email: бэк берёт email только у провайдера (Google),
     * отдельного признака привязки в ответе нет.
     */
    val canLinkGoogle: Boolean get() = isGoogleAvailable && email.isNullOrBlank()

    /**
     * Привязать Telegram можно, пока у аккаунта нет телефона: бэк добавляет телефон и Telegram вместе,
     * отдельного признака привязки в ответе нет.
     */
    val canLinkTelegram: Boolean get() = phoneNumber.isNullOrBlank()

    /** Подтвердить через Google можно, только если Google привязан (у аккаунта есть email) */
    val canProveWithGoogle: Boolean get() = isGoogleAvailable && !email.isNullOrBlank()

    /** Подтвердить через Telegram можно, только если Telegram привязан (у аккаунта есть телефон) */
    val canProveWithTelegram: Boolean get() = !phoneNumber.isNullOrBlank()

    val tryToDelete: Boolean get() = dialog == ProfileDialog.DELETE
}
