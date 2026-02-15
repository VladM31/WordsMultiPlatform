package vm.words.ua.core.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class RemoteConfigData(
    val baseUrl: String,
    val host: String,
    val telegramBotLink: String,
    val details: Set<PlatformDetail>
)

@Serializable
data class PlatformDetail(
    val platform: String,
    val version: String,
    val availableVersions: Set<String>,
    val policy: DocumentInfo,
    val instruction: DocumentInfo,
    val updateLink: String
)

@Serializable
data class DocumentInfo(
    val link: String,
    val updateAt: String
)