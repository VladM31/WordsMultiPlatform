package vm.words.ua.playlist.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class PlayListGradeRequest(
    val wordId: String,
    val wordGrade: Long
)
