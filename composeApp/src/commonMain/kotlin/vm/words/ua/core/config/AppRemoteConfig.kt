package vm.words.ua.core.config

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import vm.words.ua.core.domain.models.DocItem
import vm.words.ua.core.domain.models.DocItemType
import vm.words.ua.core.domain.models.RemoteConfigData
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.platform.isWeb


object AppRemoteConfig {
    private const val CONFIG_URL = "https://gist.githubusercontent.com/VladM31/54f9cccbe96d9adf02d32791dba4f49e/raw"

    private var configData: RemoteConfigData? = null

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }


    val baseUrl: String
        get() = configData?.baseUrl ?: "https://study-words.com"


    val instruction: DocItem
        get() {
            val type = if (currentPlatform().isWeb) {
                DocItemType.WEB
            } else {
                DocItemType.DEFAULT_TYPE
            }
            return configData?.instructions?.find { it.type == type } ?: DocItem()
        }

    val defaultInstruction: DocItem
        get() {
            return configData?.instructions?.find { it.type == DocItemType.DEFAULT_TYPE } ?: DocItem()
        }

    val policy: DocItem
        get() {
            val type = if (currentPlatform().isWeb) {
                DocItemType.WEB
            } else {
                DocItemType.DEFAULT_TYPE
            }
            return configData?.policies?.find { it.type == type } ?: DocItem()
        }

    val defaultPolicy: DocItem
        get() {
            return configData?.policies?.find { it.type == DocItemType.DEFAULT_TYPE } ?: DocItem()
        }

    val telegramBotLink: String
        get() = configData?.telegramBotLink ?: "https://t.me/needlework_number_bot"

    val updateLink: String
        get() = configData?.updateLink ?: "https://github.com/VladM31/AndroidWordApplication/releases/download/Card_Payment/Words-dev-v2.0.1.apk"

    val version: String
        get() = configData?.version ?: "0.0.1"

    val currentVersion: String = "0.0.1"

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
