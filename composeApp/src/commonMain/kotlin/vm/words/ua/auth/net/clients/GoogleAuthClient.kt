package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.google.GmailLoginRequest
import vm.words.ua.auth.net.requests.google.GoogleSingUpRequest
import vm.words.ua.auth.net.requests.google.GoogleTokenLoginRequest
import vm.words.ua.auth.net.responses.AuthResponse

interface GoogleAuthClient {

    suspend fun loginWithGoogleToken(request: GoogleTokenLoginRequest): AuthResponse

    suspend fun login(request: GmailLoginRequest): AuthResponse

    suspend fun signUp(request: GoogleSingUpRequest): AuthResponse
}