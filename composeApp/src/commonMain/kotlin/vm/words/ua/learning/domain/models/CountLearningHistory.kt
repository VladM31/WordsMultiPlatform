package vm.words.ua.learning.domain.models

import vm.words.ua.learning.domain.models.enums.LearningHistoryType

data class CountLearningHistory(
    val count: Int,
    val type: LearningHistoryType
)
