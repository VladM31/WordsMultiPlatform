package vm.words.ua.core.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Обертка для работы с Firebase Remote Config
 */
object RemoteConfigManager {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    /**
     * Инициализация Remote Config с дефолтными значениями
     */
    suspend fun initialize(defaults: Map<String, Any> = emptyMap()) {
        try {
            remoteConfig.setDefaults(*defaults.map { it.key to it.value }.toTypedArray())
            remoteConfig.fetchAndActivate()
        } catch (e: Exception) {
            println("Error initializing Remote Config: ${e.message}")
        }
    }

    /**
     * Получить строковое значение из Remote Config
     */
    fun getString(key: String, defaultValue: String = ""): String {
        return try {
            remoteConfig.getValue(key).asString()
        } catch (e: Exception) {
            println("Error getting string for key $key: ${e.message}")
            defaultValue
        }
    }

    /**
     * Получить boolean значение из Remote Config
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return try {
            remoteConfig.getValue(key).asBoolean()
        } catch (e: Exception) {
            println("Error getting boolean for key $key: ${e.message}")
            defaultValue
        }
    }

    /**
     * Получить long значение из Remote Config
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return try {
            remoteConfig.getValue(key).asLong()
        } catch (e: Exception) {
            println("Error getting long for key $key: ${e.message}")
            defaultValue
        }
    }

    /**
     * Получить double значение из Remote Config
     */
    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return try {
            remoteConfig.getValue(key).asDouble()
        } catch (e: Exception) {
            println("Error getting double for key $key: ${e.message}")
            defaultValue
        }
    }

    /**
     * Получить все значения как Flow для реактивного обновления
     */
    fun observeConfig(): Flow<Unit> = flow {
        try {
            remoteConfig.fetchAndActivate()
            emit(Unit)
        } catch (e: Exception) {
            println("Error fetching Remote Config: ${e.message}")
        }
    }
}
