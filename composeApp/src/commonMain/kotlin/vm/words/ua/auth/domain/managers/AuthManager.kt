package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.AuthResult
import vm.words.ua.auth.domain.models.SignUpModel

interface AuthManager {
    suspend fun logIn(phoneNumber: String, password: String) : AuthResult

    suspend fun signUp(req: SignUpModel) : AuthResult

    suspend fun isRegistered(phoneNumber:  String): Boolean

    suspend fun parseToken(): Boolean
}