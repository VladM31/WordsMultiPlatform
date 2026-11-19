package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.OAuthResult

interface GoogleAuthManager {

    /**
     * Вход и регистрация через Google одним вызовом.
     * Если аккаунта нет, бэк создаёт его сам (имя берётся из токена, пароля нет).
     */
    suspend fun login(): OAuthResult
}
