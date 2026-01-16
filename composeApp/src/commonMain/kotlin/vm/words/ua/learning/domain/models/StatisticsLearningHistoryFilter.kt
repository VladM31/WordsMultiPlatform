package vm.words.ua.learning.domain.models

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.Range
import vm.words.ua.core.domain.models.filters.Queryable

@Serializable
data class StatisticsLearningHistoryFilter(
    val date: Range<String>? = null
) : Queryable
