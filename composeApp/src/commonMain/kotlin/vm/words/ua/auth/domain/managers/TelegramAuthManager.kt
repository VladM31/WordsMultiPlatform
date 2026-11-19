package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.OwnershipPurpose
import vm.words.ua.auth.domain.models.TelegramAuthResult
import vm.words.ua.auth.domain.models.TelegramAuthSession
import vm.words.ua.auth.domain.models.enums.TelegramCodeStatus
import vm.words.ua.auth.domain.models.enums.TelegramMiniAppLoginStatus

interface TelegramAuthManager {

    suspend fun startLogin(phoneNumber: String): TelegramAuthResult

    /**
     * Запрашивает код у бота для подтверждения владения аккаунтом (смена пароля, удаление).
     * В отличие от [startLogin] ничего не сохраняет в сессию входа, а сам код нельзя
     * использовать в [login]: его проверяет бэк при смене пароля или удалении.
     */
    suspend fun startProof(phoneNumber: String, purpose: OwnershipPurpose): TelegramAuthResult

    suspend fun login(phoneNumber: String, code: String): Boolean

    /** Где находится код подтверждения, сам код при этом не расходуется */
    suspend fun codeStatus(code: String): TelegramCodeStatus

    suspend fun loginWithMiniApp(initData: String): TelegramMiniAppLoginStatus

    val session: TelegramAuthSession?

    /** Сбросить сохранённую сессию входа (код, номер, срок) */
    fun cancelSession()

}