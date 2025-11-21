package vm.words.ua.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class DocItem(
    val type: DocItemType = DocItemType.DEFAULT_TYPE,
    val link: String = "",
    val updateAt: String = ""
)

enum class DocItemType {
    WEB,
    DEFAULT_TYPE
}

@Serializable
data class RemoteConfigData(
    val baseUrl: String = "",
    val policies: List<DocItem> = emptyList(),
    val instructions: List<DocItem> = emptyList(),
    val updateLink: String = "",
    val localHost: String = "",
    val version: String = "",
    val telegramBotLink: String = ""
)