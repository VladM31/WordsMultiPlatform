package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.AuthResult

interface AuthManager {
    /**
     * Вход по паролю.
     * @param login телефон в любом формате или email
     */
    suspend fun logIn(login: String, password: String): AuthResult

    suspend fun isRegistered(phoneNumber:  String): Boolean

    suspend fun parseToken(): Boolean
}
