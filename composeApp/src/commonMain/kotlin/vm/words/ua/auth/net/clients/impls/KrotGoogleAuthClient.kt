package vm.words.ua.auth.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import vm.words.ua.auth.net.clients.GoogleAuthClient
import vm.words.ua.auth.net.requests.google.GoogleSignInRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.utils.throwIfError

class KrotGoogleAuthClient(
    private val httpClient: HttpClient
) : GoogleAuthClient {

    private val baseUrl by lazy {
        "${AppRemoteConfig.baseUrl}/auth/google"
    }

    override suspend fun signIn(request: GoogleSignInRequest): AuthResponse {
        return httpClient.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.throwIfError().body()
    }
}
