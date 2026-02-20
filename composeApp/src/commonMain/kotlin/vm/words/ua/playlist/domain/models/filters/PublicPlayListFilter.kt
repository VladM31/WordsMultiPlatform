package vm.words.ua.playlist.domain.models.filters

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.filters.Queryable
import vm.words.ua.playlist.domain.models.enums.PlaylistSortField

@Serializable
data class PublicPlayListFilter(
    val ids: Set<String>? = null,
    val name: String? = null,
    val sortField: PlaylistSortField = PlaylistSortField.NAME,
    val asc: Boolean = true,
    val page: Int = 0,
    val size: Int = 20
) : Queryable