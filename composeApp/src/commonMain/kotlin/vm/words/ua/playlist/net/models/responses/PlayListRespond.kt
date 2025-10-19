package vm.words.ua.playlist.net.models.responses

import kotlinx.serialization.Serializable




@Serializable
data class PlayListRespond(
    val id: String,
    val userId: String,
    val name: String,
    val createdAt: String,
    val words: List<PinnedWordResponse>
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
        val lang: String,
        val translate: String,
        val translateLang: String,
        val cefr: String,
        val description: String?,
        val category: String?,
        val soundLink: String?,
        val imageLink: String?,
        val type: String,
        val createdAt: String
    )
}

