package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.LoginRequest
import vm.words.ua.auth.net.requests.SignUpRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.auth.net.responses.SignUpResponse

/**
 * Интерфейс клиента для работы с API авторизации
 */
interface AuthClient {
    /**
     * Вход в систему
     */
    suspend fun logIn(request: LoginRequest): AuthResponse

    /**
     * Регистрация нового пользователя
     */
    suspend fun signUp(request: SignUpRequest): SignUpResponse

    /**
     * Проверка, зарегистрирован ли пользователь
     */
    suspend fun isRegistered(phoneNumber: String): Boolean

    /**
     * Парсинг токена для получения информации о пользователе
     */
    suspend fun parseToken(token: String): AuthResponse.User
}

