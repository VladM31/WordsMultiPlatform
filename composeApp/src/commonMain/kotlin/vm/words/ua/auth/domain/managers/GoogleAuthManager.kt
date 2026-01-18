package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.AuthResult
import vm.words.ua.auth.domain.models.google.GmailLoginDto
import vm.words.ua.auth.domain.models.google.GoogleSingUpDto

interface GoogleAuthManager {

    suspend fun login(): AuthResult

    suspend fun login(dto: GmailLoginDto): AuthResult

    suspend fun signUp(dto: GoogleSingUpDto): AuthResult
}