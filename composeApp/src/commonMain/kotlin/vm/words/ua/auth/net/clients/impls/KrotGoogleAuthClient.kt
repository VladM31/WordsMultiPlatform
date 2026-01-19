package vm.words.ua.auth.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import vm.words.ua.auth.net.clients.GoogleAuthClient
import vm.words.ua.auth.net.requests.google.GmailLoginRequest
import vm.words.ua.auth.net.requests.google.GoogleSingUpRequest
import vm.words.ua.auth.net.requests.google.GoogleTokenLoginRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.utils.throwIfError

class KrotGoogleAuthClient(
    private val httpClient: HttpClient
) : GoogleAuthClient {

    private val baseUrl by lazy {
        "${AppRemoteConfig.baseUrl}/auth/google"
    }

    override suspend fun loginWithGoogleToken(request: GoogleTokenLoginRequest): AuthResponse {
        val respond = httpClient.post("${baseUrl}/login/token") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.throwIfError()
        return respond.body()
    }

    override suspend fun login(request: GmailLoginRequest): AuthResponse {
        val respond = httpClient.post("${baseUrl}/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.throwIfError()
        return respond.body()
    }

    override suspend fun signUp(request: GoogleSingUpRequest): AuthResponse {
        val respond = httpClient.post("${baseUrl}/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.throwIfError()
        return respond.body()
    }

}