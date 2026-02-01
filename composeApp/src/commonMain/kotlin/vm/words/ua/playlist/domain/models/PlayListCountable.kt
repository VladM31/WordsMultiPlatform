package vm.words.ua.playlist.domain.models

import kotlinx.datetime.Instant
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

interface PlayListCountable {
    val id: String
    val name: String
    val createdAt: Instant
    val count: Long
    val tags: Set<String>?
    val cefrs: Set<CEFR>?
    val language: Language?
    val translateLanguage: Language?
}