package vm.words.ua.exercise.domain.models.data

import kotlinx.datetime.Instant
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

data class ExerciseWordDetails(
    val userWordId: String,
    val wordId: String,
    val transactionId: String,
    val grade: Int,
    val createdAt: Instant,
    val lastReadDate: Instant,
    val original: String,
    val translate: String,
    val lang: Language,
    val translateLang: Language,
    val cefr: CEFR,
    val description: String?,
    val category: String?,
    val soundLink: String?,
    val imageLink: String?,
    val soundContent: ByteContent? = null,
    val imageContent: ByteContent? = null
)