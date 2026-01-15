package vm.words.ua.learning.domain.models

import kotlinx.datetime.LocalDate
import vm.words.ua.learning.domain.models.enums.LearningHistoryType

data class StatisticsLearningHistory(
    val count: Int,
    val grades: Long,
    val type: LearningHistoryType,
    val date: LocalDate
)