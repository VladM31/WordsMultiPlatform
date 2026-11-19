package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.DeleteOptions
import vm.words.ua.auth.domain.models.OwnershipProof
import vm.words.ua.auth.domain.models.OwnershipPurpose
import vm.words.ua.auth.domain.models.enums.TelegramCodeStatus

/**
 * Операции с текущим пользователем. Ошибки бэкенда (неверный пароль, 429, 409 и т.п.)
 * пробрасываются как [vm.words.ua.core.net.exceptions.ApiException] с готовым текстом.
 */
interface UserManager {

    /** Удаляет аккаунт. Нужен [DeleteOptions.password] или [DeleteOptions.proof]. */
    suspend fun delete(options: DeleteOptions)

    /** Меняет имя и фамилию, сохраняет нового пользователя и новый токен из ответа. */
    suspend fun editName(firstName: String, lastName: String)

    /**
     * Устанавливает или меняет пароль.
     * Для смены нужен [oldPassword] или [proof], для первого пароля только [proof].
     */
    suspend fun setPassword(newPassword: String, oldPassword: String? = null, proof: OwnershipProof? = null)

    /** Привязывает Google к текущему аккаунту (запрашивает вход Google на устройстве). */
    suspend fun linkGoogle()

    /** Доступен ли вход через Google на этом устройстве */
    val isGoogleAvailable: Boolean

    /** Получает свежий Google `idToken` как доказательство владения. */
    suspend fun requestGoogleProof(): OwnershipProof

    /**
     * Запускает подтверждение через Telegram: бот отправит сообщение с кнопками Yes и No.
     * @return код, который после нажатия Yes используется как [OwnershipProof.telegram]
     */
    suspend fun requestTelegramProofCode(purpose: OwnershipPurpose): String

    /** Нажал ли пользователь Yes в боте. Опрашивать можно сколько угодно, код не расходуется. */
    suspend fun telegramProofStatus(code: String): TelegramCodeStatus
}
