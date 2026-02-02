package vm.words.ua.playlist.net.models.requests

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.filters.Queryable
import vm.words.ua.playlist.domain.models.enums.PlaylistSortField

@Serializable
data class PublicPlayListGetRequest(
    val ids: Set<String>?,
    val name: String?,
    val sortField: PlaylistSortField,
    val asc: Boolean,
    val page: Int,
    val size: Int
) : Queryable