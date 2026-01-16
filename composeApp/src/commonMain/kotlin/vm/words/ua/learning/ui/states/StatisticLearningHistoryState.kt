package vm.words.ua.learning.ui.states

import kotlinx.datetime.Instant
import vm.words.ua.learning.domain.models.StatisticsLearningHistory

data class StatisticLearningHistoryState(
    val toDate: Instant,
    val statistic: List<StatisticsLearningHistory> = emptyList()
)