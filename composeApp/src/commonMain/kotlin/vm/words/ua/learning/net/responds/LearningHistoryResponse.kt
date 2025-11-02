package vm.words.ua.learning.net.responds

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.learning.domain.models.enums.LearningHistoryType

@Serializable
data class LearningHistoryResponse(
    val id: String,
    val userId: String,
    val wordId: String,
    val original: String,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
    val date: String,
    val type: LearningHistoryType,
    val grade: Int,
)
