package vm.words.ua.learning.domain.models

import kotlinx.datetime.LocalDate
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.learning.domain.models.enums.LearningHistoryType

data class LearningHistory(
    val id: String,
    val wordId: String,
    val original: String,
    val nativeLang: Language,
    val learningLang: Language,
    val cefr: CEFR,
    val date: LocalDate,
    val type: LearningHistoryType,
    val grade: Int,
)
