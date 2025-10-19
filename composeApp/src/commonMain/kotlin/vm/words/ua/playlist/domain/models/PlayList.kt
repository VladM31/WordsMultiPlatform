package vm.words.ua.playlist.domain.models

import kotlinx.datetime.Instant

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

    data class UserWord(
        val id: String,
        val learningGrade: Long,
        val createdAt: Instant,
        val lastReadDate: Instant,
        val word: Word
    )

    data class Word(
        val id: String,
        val original: String,
        val lang: String,
        val translate: String,
        val translateLang: String,
        val cefr: String,
        val description: String?,
        val category: String?,
        val soundLink: String?,
        val imageLink: String?
    )

}