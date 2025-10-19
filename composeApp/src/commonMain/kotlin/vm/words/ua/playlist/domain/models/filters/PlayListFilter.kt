package vm.words.ua.playlist.domain.models.filters

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.filters.Queryable
import vm.words.ua.playlist.domain.models.enums.PlaylistSortField


@Serializable
data class PlayListFilter(
    val ids: List<String>? = null,
    val userIds: List<String>? = null,
    val name: String? = null,
    val sortField: PlaylistSortField = PlaylistSortField.CREATED_AT,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20
) : Queryable

