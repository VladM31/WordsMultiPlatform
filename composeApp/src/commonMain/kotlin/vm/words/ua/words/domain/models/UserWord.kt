package vm.words.ua.words.domain.models

import kotlinx.datetime.Instant

data class UserWord(
    val id: String,
    val learningGrade: Long,
    val createdAt: Instant,
    val lastReadDate: Instant,
    val word: Word
)