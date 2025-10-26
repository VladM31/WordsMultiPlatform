package vm.words.ua.core.domain.models

import kotlinx.serialization.Serializable

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