package vm.words.ua.playlist.domain.models.filters

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.filters.Queryable

@Serializable
data class DeletePlayListFilter(
    val ids: List<String>
) : Queryable
