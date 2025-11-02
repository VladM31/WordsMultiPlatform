package vm.words.ua.learning.net.responds

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

@Serializable
data class LearningPlanResponse(
    val wordsPerDay: Int,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
    val createdAt: Instant,
    val updatedAt: Instant
)
