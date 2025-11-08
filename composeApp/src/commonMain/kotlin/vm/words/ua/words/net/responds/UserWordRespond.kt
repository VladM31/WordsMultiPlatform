package vm.words.ua.words.net.responds

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserWordRespond(
    val id: String,
    val learningGrade: Long,
    val createdAt: Instant,
    val lastReadDate: Instant,
    val word: WordRespond,
)
