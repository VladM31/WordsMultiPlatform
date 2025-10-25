package vm.words.ua.core.firebase

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import vm.words.ua.core.config.RemoteConfigData

/**
 * Менеджер для работы с удаленной конфигурацией приложения
 */
object AppRemoteConfig {
    private const val CONFIG_URL = "https://gist.githubusercontent.com/VladM31/54f9cccbe96d9adf02d32791dba4f49e/raw"

    private var configData: RemoteConfigData? = null

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val baseUrl: String
        get() = configData?.baseUrl ?: "https://study-words.com"

    val instructionLastUpdate: String
        get() = configData?.instructionLastUpdate ?: "2025-10-06T22:35:29.604559800+03:00"

    val instructionLink: String
        get() = configData?.instructionLink ?: "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/instruction/en_instruction.pdf"

    val localHost: String
        get() = configData?.localHost ?: "vps2498837.fastwebserver.de"

    val policyLastUpdateAt: String
        get() = configData?.policyLastUpdateAt ?: "2025-10-12T01:52:29.604559800+03:00"

    val policyLink: String
        get() = configData?.policyLink ?: "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/policy/en_policy.pdf"

    val telegramBotLink: String
        get() = configData?.telegramBotLink ?: "https://t.me/needlework_number_bot"

    val updateLink: String
        get() = configData?.updateLink ?: "https://github.com/VladM31/AndroidWordApplication/releases/download/Card_Payment/Words-dev-v2.0.1.apk"

    val version: String
        get() = configData?.version ?: "2.0.1"

    /**
     * Инициализация конфигурации - загрузка данных из удаленного источника
     */
    suspend fun initialize() {
        refresh()
    }

    /**
     * Обновить значения из удаленного источника
     */
    suspend fun refresh() {
        try {
            val client = createHttpClient()
            val response: HttpResponse = client.get(CONFIG_URL)
            val jsonString = response.bodyAsText()
            configData = json.decodeFromString<RemoteConfigData>(jsonString)
            client.close()
            println("Remote config loaded successfully")
        } catch (e: Exception) {
            println("Error loading remote config: ${e.message}")
            println("Using default values")
        }
    }
}

/**
 * Создание HttpClient для разных платформ
 */
expect fun createHttpClient(): HttpClient
