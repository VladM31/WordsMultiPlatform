package vm.words.ua.core.config

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import vm.words.ua.core.domain.models.DocItem
import vm.words.ua.core.domain.models.DocItemType
import vm.words.ua.core.domain.models.RemoteConfigData
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.utils.storage.AppStorage
import vm.words.ua.utils.storage.managers.KeyValueStorage
import vm.words.ua.utils.storage.models.StorageSecurity

object AppRemoteConfig {
    private const val CONFIG_URL = "https://gist.githubusercontent.com/VladM31/54f9cccbe96d9adf02d32791dba4f49e/raw"
    private const val STORAGE_NAME = "app_remote_config"
    private const val STORAGE_JSON_KEY = "remote_config_json"
    private const val DEFAULT_BASE_URL = "https://study-words.com"
    private const val DEFAULT_TELEGRAM_BOT = "https://t.me/needlework_number_bot"
    private const val DEFAULT_UPDATE_LINK = "https://study-words.com/"
    private const val DEFAULT_VERSION = "1.0.1"

    const val CURRENT_VERSION = "1.0.0"

    private var configData: RemoteConfigData? = null
        get() {
            if (field == null) {
                field = getCachedConfig()
            }
            return field
        }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val storage: KeyValueStorage by lazy {
        AppStorage.create(STORAGE_NAME, StorageSecurity.NON_SECURITY)
    }

    val baseUrl: String
        get() = configData?.baseUrl ?: DEFAULT_BASE_URL

    val currentVersion: String
        get() = CURRENT_VERSION

    val instruction: DocItem
        get() = getDocItemByPlatform(configData?.instructions)

    val defaultInstruction: DocItem
        get() = getDefaultDocItem(configData?.instructions)

    val policy: DocItem
        get() = getDocItemByPlatform(configData?.policies)

    val defaultPolicy: DocItem
        get() = getDefaultDocItem(configData?.policies)

    val telegramBotLink: String
        get() = configData?.telegramBotLink ?: DEFAULT_TELEGRAM_BOT

    val updateLink: String
        get() = configData?.updateLink ?: DEFAULT_UPDATE_LINK

    val version: String
        get() = configData?.version ?: DEFAULT_VERSION


    suspend fun initialize() {
        refresh()
    }

    suspend fun refresh() {
        try {
            loadFromRemote()
        } catch (e: Exception) {
            println("Error loading remote config: ${e.message}")
            println("Attempting to load config from cache")
            loadFromCache()
        }
    }

    private suspend fun loadFromRemote() {
        val client = createHttpClient()
        try {
            val response: HttpResponse = client.get(CONFIG_URL)
            val jsonString = response.bodyAsText()

            configData = json.decodeFromString<RemoteConfigData>(jsonString)
            cacheConfig(jsonString)

            println("Remote config loaded successfully")
        } finally {
            client.close()
        }
    }

    private fun loadFromCache() {
        try {
            val cached = storage.getString(STORAGE_JSON_KEY)
            if (cached.isNullOrBlank()) {
                println("No cached config found. Using default values")
                return
            }

            configData = json.decodeFromString<RemoteConfigData>(cached)
            println("Loaded remote config from cache")
        } catch (e: Exception) {
            println("Error reading cached config: ${e.message}")
            println("Using default values")
        }
    }

    private fun cacheConfig(jsonString: String) {
        try {
            storage.putString(STORAGE_JSON_KEY, jsonString)
        } catch (_: Throwable) {
        }
    }

    private fun getCachedConfig(): RemoteConfigData? {
        return try {
            val cached = storage.getString(STORAGE_JSON_KEY) ?: return null
            json.decodeFromString<RemoteConfigData>(cached)
        } catch (e: Exception) {
            null
        }
    }

    private fun getCurrentPlatformType(): DocItemType {
        return if (currentPlatform() == AppPlatform.WASM) {
            DocItemType.WEB
        } else {
            DocItemType.DEFAULT_TYPE
        }
    }

    private fun getDocItemByPlatform(items: List<DocItem>?): DocItem {
        val type = getCurrentPlatformType()
        return items?.find { it.type == type } ?: DocItem()
    }

    private fun getDefaultDocItem(items: List<DocItem>?): DocItem {
        return items?.find { it.type == DocItemType.DEFAULT_TYPE } ?: DocItem()
    }
}