package vm.words.ua.settings.ui.states

import vm.words.ua.auth.domain.models.OwnershipProof
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState

enum class ProfileDialog {
    NONE, EDIT_NAME, PASSWORD, DELETE
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
    /** Задан ли пароль: `false` скрывает поля текущего пароля, `null` пока неизвестно */
    val hasPassword: Boolean? = null,
    val isGoogleAvailable: Boolean = false,
    /** Telegram-подтверждение возможно: внутри Mini App или у аккаунта есть телефон */
    val isTelegramProofAvailable: Boolean = false,
    /** Сообщение об успешном действии */
    val message: String? = null,
    override val errorMessage: ErrorMessage? = null,
) : ErrorableState {

    /**
     * Привязать Google можно, пока у аккаунта нет email: бэк берёт email только у провайдера (Google),
     * отдельного признака привязки в ответе нет.
     */
    val canLinkGoogle: Boolean get() = isGoogleAvailable && email.isNullOrBlank()

    val tryToDelete: Boolean get() = dialog == ProfileDialog.DELETE
}
