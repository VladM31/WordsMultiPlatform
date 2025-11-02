package vm.words.ua.learning.net.responds

import kotlinx.serialization.Serializable
import vm.words.ua.learning.domain.models.enums.LearningHistoryType

@Serializable
data class CountLearningHistoryResponse(
    val count: Int,
    val type: LearningHistoryType
)
