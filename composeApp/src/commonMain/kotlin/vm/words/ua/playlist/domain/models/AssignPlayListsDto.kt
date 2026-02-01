package vm.words.ua.playlist.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AssignPlayListsDto(
    val playListIds: Set<String>
)
