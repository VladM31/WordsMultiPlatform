package vm.words.ua.auth.net.clients.impls

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import vm.words.ua.auth.net.clients.AuthClient
import vm.words.ua.auth.net.requests.LoginRequest
import vm.words.ua.auth.net.requests.SignUpRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.auth.net.responses.SignUpResponse
import vm.words.ua.core.config.AppRemoteConfig

/**
 * Реализация AuthClient на основе Ktor HTTP клиента
 */
class KtorAuthClient(
    private val client: HttpClient
) : AuthClient {


    override suspend fun logIn(request: LoginRequest): AuthResponse {
        return try {
            val response: HttpResponse = client.post("${AppRemoteConfig.baseUrl}/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                response.body<AuthResponse>()
            } else {
                AuthResponse(
                    error = AuthResponse.Error(
                        message = "Error: ${response.status.value}, body: ${response.bodyAsText()}"
                    )
                )
            }
        } catch (e: Exception) {
            AuthResponse(
                error = AuthResponse.Error(
                    message = "Error: ${e.message}"
                )
            )
        }
    }

    override suspend fun signUp(request: SignUpRequest): SignUpResponse {
        return try {
            val response: HttpResponse = client.post("${AppRemoteConfig.baseUrl}/auth/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val success = response.bodyAsText().toBoolean()
                SignUpResponse(success = success)
            } else {
                SignUpResponse.Companion.error(
                    message = "Error: ${response.status.value}, body: ${response.bodyAsText()}"
                )
            }
        } catch (e: Exception) {
            SignUpResponse.Companion.error(message = "Error: ${e.message}")
        }
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