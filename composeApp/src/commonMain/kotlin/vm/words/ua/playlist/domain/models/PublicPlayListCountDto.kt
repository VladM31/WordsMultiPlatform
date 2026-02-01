package vm.words.ua.playlist.domain.models

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

@Serializable
data class PublicPlayListCountDto(
    val id: String,
    val name: String,
    val createdAt: String,
    val count: Long,
    val tags: Set<String>?,
    val cefrs: Set<CEFR>?,
    val language: Language?,
    val translateLanguage: Language?
)