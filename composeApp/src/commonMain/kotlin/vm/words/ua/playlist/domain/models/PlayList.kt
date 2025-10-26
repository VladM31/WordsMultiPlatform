package vm.words.ua.playlist.domain.models

import kotlinx.datetime.Instant
import vm.words.ua.words.domain.models.UserWord

data class PlayList(
    val id: String,
    val name: String,
    val createdAt: Instant,
    val words: List<PinnedWord>
){
    data class PinnedWord(
        val learningGrade: Long,
        val lastReadDate: Instant,
        val userWord: UserWord
    )
}