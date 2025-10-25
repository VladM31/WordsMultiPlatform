package vm.words.ua.core.config

import kotlinx.serialization.Serializable

/**
 * Модель данных для конфигурации из удаленного источника
 */
@Serializable
data class RemoteConfigData(
    val baseUrl: String,
    val policyLink: String,
    val updateLink: String,
    val instructionLastUpdate: String,
    val policyLastUpdateAt: String,
    val instructionLink: String,
    val localHost: String,
    val version: String,
    val telegramBotLink: String
)

