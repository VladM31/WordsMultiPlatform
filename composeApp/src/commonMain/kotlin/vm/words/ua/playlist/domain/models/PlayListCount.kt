package vm.words.ua.playlist.domain.models

import kotlinx.datetime.Instant

data class PlayListCount(
    val id: String,
    val name: String,
    val createdAt: Instant,
    val count: Long
)