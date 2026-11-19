package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.google.GoogleSignInRequest
import vm.words.ua.auth.net.responses.AuthResponse

interface GoogleAuthClient {

    /**
     * `POST /auth/google`: вход и регистрация одним методом.
     * Ошибки приходят как [vm.words.ua.core.net.exceptions.ApiException].
     */
    suspend fun signIn(request: GoogleSignInRequest): AuthResponse
}
