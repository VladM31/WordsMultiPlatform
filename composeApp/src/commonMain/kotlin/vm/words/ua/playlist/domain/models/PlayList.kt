package vm.words.ua.playlist.domain.models

import kotlinx.datetime.Instant
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.UserWord

data class PlayList(
    val id: String,
    val name: String,
    val createdAt: Instant,
    val words: List<PinnedWord>,
    val tags: Set<String>?,
    val cefrs: Set<CEFR>?,
    val language: Language?,
    val translateLanguage: Language?
){
    data class PinnedWord(
        val learningGrade: Long,
        val lastReadDate: Instant,
        val userWord: UserWord
    )
}