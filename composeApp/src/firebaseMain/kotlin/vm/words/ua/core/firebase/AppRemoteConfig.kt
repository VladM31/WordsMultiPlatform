package vm.words.ua.core.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig

/**
 * Реальная реализация Remote Config для JVM и JS платформ
 */
actual object AppRemoteConfig {
    private val remoteConfig = Firebase.remoteConfig

    // Дата последнего обновления инструкции
    actual val instructionLastUpdate: String
        get() = remoteConfig.get<String>("instruction_last_update")

    // Ссылка на инструкцию
    actual val instructionLink: String
        get() = remoteConfig.get<String>("instruction_link")

    // Адрес локального хоста/сервера
    actual val localHost: String
        get() = remoteConfig.get<String>("local_host")

    // Дата последнего обновления политики
    actual val policyLastUpdateAt: String
        get() = remoteConfig.get<String>("policy_last_update_at")

    // Ссылка на политику конфиденциальности
    actual val policyLink: String
        get() = remoteConfig.get<String>("policy_link")

    // Ссылка на Telegram бота
    actual val telegramBotLink: String
        get() = remoteConfig.get<String>("telegram_bot_link")

    // Ссылка на обновление приложения
    actual val updateLink: String
        get() = remoteConfig.get<String>("update_link")

    // Версия приложения
    actual val version: String
        get() = remoteConfig.get<String>("version")

    /**
     * Инициализация Remote Config с дефолтными значениями
     */
    actual suspend fun initialize() {
        try {
            val defaults = arrayOf(
                "instruction_last_update" to "2025-10-06T22:35:29.604559800+03:00",
                "instruction_link" to "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/instruction.txt",
                "local_host" to "vps2498837.fastwebserver.de",
                "policy_last_update_at" to "2025-10-12T01:52:29.604559800+03:00",
                "policy_link" to "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/policy.txt",
                "telegram_bot_link" to "https://t.me/needlework_number_bot",
                "update_link" to "https://github.com/VladM31/AndroidWordApplication/releases/download/Card_Payment/Words-dev-v2.0.1.apk",
                "version" to "2.0.1"
            )

            remoteConfig.setDefaults(*defaults)
            remoteConfig.fetchAndActivate()
        } catch (e: Exception) {
            println("Error initializing Remote Config: ${e.message}")
        }
    }

    /**
     * Обновить значения из Firebase
     */
    actual suspend fun refresh() {
        try {
            remoteConfig.fetchAndActivate()
        } catch (e: Exception) {
            println("Error refreshing Remote Config: ${e.message}")
        }
    }
}
