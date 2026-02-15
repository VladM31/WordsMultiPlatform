package vm.words.ua.core.config

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import vm.words.ua.core.domain.models.DocumentInfo
import vm.words.ua.core.domain.models.PlatformDetail
import vm.words.ua.core.domain.models.RemoteConfigData
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.utils.storage.AppStorage
import vm.words.ua.utils.storage.managers.KeyValueStorage
import vm.words.ua.utils.storage.models.StorageSecurity

object AppRemoteConfig {
    private const val CONFIG_URL = "https://gist.githubusercontent.com/VladM31/b2ca69b294f53ac8f1fb35c10691d12e/raw"
    private const val STORAGE_NAME = "app_remote_config"
    private const val STORAGE_JSON_KEY = "remote_config_json"
    private const val DEFAULT_BASE_URL = "https://study-words.com"
    private const val DEFAULT_TELEGRAM_BOT = "https://t.me/needlework_number_bot"

    const val CURRENT_VERSION = "1.1.0"

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

    val instruction: DocumentInfo
        get() = getPlatformDetails().instruction

    val defaultInstruction: DocumentInfo
        get() = getUnknowPlatformDetails().instruction

    val policy: DocumentInfo
        get() = getPlatformDetails().policy

    val defaultPolicy: DocumentInfo
        get() = getUnknowPlatformDetails().policy

    val telegramBotLink: String
        get() = configData?.telegramBotLink ?: DEFAULT_TELEGRAM_BOT

    val updateLink: String
        get() = getPlatformDetails().updateLink

    val version: String
        get() = getPlatformDetails().version

    val availableVersions: Set<String>
        get() = getPlatformDetails().availableVersions


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

    private fun getPlatformDetails(): PlatformDetail {
        val platform = currentPlatform()
        if (configData == null) {
            loadFromCache()
        }
        return configData?.details?.find { it.platform == platform.name }
            ?: throw IllegalStateException("No platform details found for ${platform.name}")
    }

    private fun getUnknowPlatformDetails(): PlatformDetail {
        return configData?.details?.find { it.platform == AppPlatform.UNKNOWN.name }
            ?: throw IllegalStateException("No platform details found for ${AppPlatform.UNKNOWN}")
    }
}