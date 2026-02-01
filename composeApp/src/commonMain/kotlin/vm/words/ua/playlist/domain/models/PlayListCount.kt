package vm.words.ua.playlist.domain.models

import kotlinx.datetime.Instant
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

data class PlayListCount(
    override val id: String,
    override val name: String,
    override val createdAt: Instant,
    override val count: Long,
    override val tags: Set<String>?,
    override val cefrs: Set<CEFR>?,
    override val language: Language?,
    override val translateLanguage: Language?
) : PlayListCountable