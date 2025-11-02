package vm.words.ua.learning.net.responds

import kotlinx.serialization.Serializable
import vm.words.ua.learning.domain.models.enums.LearningHistoryType

@Serializable
data class StatisticsLearningHistoryResponse(
    val count: Int,
    val grades: Long,
    val type: LearningHistoryType,
    val date: String
)
