package vm.words.ua.playlist.net.responses

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language


@Serializable
data class PlayListRespond(
    val id: String,
    val userId: String,
    val name: String,
    val createdAt: String,
    val words: List<PinnedWordResponse>,
    val tags: Set<String>?,
    val cefrs: Set<CEFR>?,
    val language: Language?,
    val translateLanguage: Language?
) {
    @Serializable
    data class PinnedWordResponse(
        val learningGrade: Long,
        val createdAt: String,
        val lastReadDate: String,
        val word: UserWordResponse
    )

    @Serializable
    data class UserWordResponse(
        val id: String,
        val userId: String,
        val learningGrade: Long,
        val createdAt: String,
        val lastReadDate: String,
        val word: WordResponse
    )

    @Serializable
    data class WordResponse(
        val id: String,
        val original: String,
        val lang: Language,
        val translate: String,
        val translateLang: Language,
        val cefr: CEFR,
        val description: String?,
        val category: String?,
        val soundLink: String?,
        val imageLink: String?,
        val type: String,
        val createdAt: String
    )
}

