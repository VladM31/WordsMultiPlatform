package vm.words.ua.core.net

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun createHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            // Настройка логирования (опционально)
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }

            // 401 для запроса с токеном: токен недействителен или просрочен
            HttpResponseValidator {
                validateResponse { response ->
                    val hasToken = response.request.headers.contains(HttpHeaders.Authorization)
                    if (response.status == HttpStatusCode.Unauthorized && hasToken) {
                        UnauthorizedHandler.onUnauthorized?.invoke()
                    }
                }
            }
        }
    }
}
