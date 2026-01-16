package vm.words.ua.learning.ui.actions

import kotlinx.datetime.Instant

sealed interface StatisticLearningHistoryAction {
    data class SetDate(val date: Instant) : StatisticLearningHistoryAction
}