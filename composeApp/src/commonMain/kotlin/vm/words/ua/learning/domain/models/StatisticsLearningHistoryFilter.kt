package vm.words.ua.learning.domain.models

import kotlinx.datetime.Instant
import vm.words.ua.core.domain.models.Range
import vm.words.ua.core.domain.models.filters.Queryable

data class StatisticsLearningHistoryFilter(
    val date: Range<Instant>? = null
) : Queryable
