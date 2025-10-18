package vm.words.ua.auth.net

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Фабрика для создания HTTP клиента с настройками
 */
object HttpClientFactory {

    fun createAuthClient(): HttpClient {
        return HttpClient {
            // Настройка JSON сериализации
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
        }
    }
}

