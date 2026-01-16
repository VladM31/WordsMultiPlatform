package vm.words.ua.learning.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.Range
import vm.words.ua.core.domain.models.filters.Queryable

@Serializable
data class LearningHistoryFilter(
    val wordIds: Collection<String>? = null,
    val date: Range<LocalDate>? = null,
    val page: Int = 0,
    val size: Int = 20,
) : Queryable