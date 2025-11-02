package vm.words.ua.learning.net.requests

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

@Serializable
data class LearningPlanRequest(
    val wordsPerDay: Int,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
)