package vm.words.ua.learning.domain.models

import kotlinx.datetime.Instant
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

data class LearningPlan(
    val wordsPerDay: Int,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
    val createdAt: Instant
)