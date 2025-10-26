package vm.words.ua.playlist.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlayList(
    val id: String,
    val name: String
)
