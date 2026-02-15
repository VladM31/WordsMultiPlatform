package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.LoginRequest
import vm.words.ua.auth.net.requests.SignUpRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.auth.net.responses.SignUpResponse


interface AuthClient {

    suspend fun logIn(request: LoginRequest): AuthResponse

    suspend fun signUp(request: SignUpRequest): SignUpResponse

    suspend fun isRegistered(phoneNumber: String): Boolean

    suspend fun parseToken(token: String): AuthResponse.User
}

