package vm.words.ua.playlist.net.models.requests

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.Range
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.domain.models.filters.Queryable
import vm.words.ua.playlist.domain.models.enums.PublicPlaylistSortField

@Serializable
data class PublicPlayListCountRequest(
    val ids: Set<String>? = null,
    val notInIds: Set<String>? = null,
    val cefrs: Set<CEFR>? = null,
    val tags: Set<String>? = null,
    val language: Language? = null,
    val translateLanguage: Language? = null,
    val associationId: String? = null,

    val name: String? = null,

    val count: Range<Long>? = null,
    val sortField: PublicPlaylistSortField = PublicPlaylistSortField.CREATED_AT,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20
) : Queryable
