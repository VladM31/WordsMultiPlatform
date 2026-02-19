package vm.words.ua.playlist.net.responds

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

@Serializable
data class PlayListCountRespond(
    val id: String,
    val userId: String,
    val name: String,
    val createdAt: String,
    val count: Long,
    val tags: Set<String>?,
    val cefrs: Set<CEFR>?,
    val language: Language?,
    val translateLanguage: Language?
)