package vm.words.ua.auth.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import vm.words.ua.auth.net.clients.AuthClient
import vm.words.ua.auth.net.requests.LoginRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.utils.throwIfError

/**
 * Реализация AuthClient на основе Ktor HTTP клиента
 */
class KtorAuthClient(
    private val client: HttpClient
) : AuthClient {

    /**
     * `POST /auth/login`. Ошибки (400, 404, 429 и другие) приходят как
     * [vm.words.ua.core.net.exceptions.ApiException] с текстом бэкенда.
     */
    override suspend fun logIn(request: LoginRequest): AuthResponse {
        return client.post("${AppRemoteConfig.baseUrl}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.throwIfError().body()
    }

    override suspend fun isRegistered(phoneNumber: String): Boolean {
        return try {
            val response: HttpResponse =
                client.get("${AppRemoteConfig.baseUrl}/auth/is-registered") {
                    parameter("phoneNumber", phoneNumber)
                }

            if (response.status.isSuccess()) {
                response.bodyAsText().toBoolean()
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun parseToken(token: String): AuthResponse.User {
        TODO("Not yet implemented")
    }
}
