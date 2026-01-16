package vm.words.ua.learning.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import vm.words.ua.core.domain.models.Range
import vm.words.ua.learning.domain.managers.LearningHistoryManager
import vm.words.ua.learning.domain.models.StatisticsLearningHistoryFilter
import vm.words.ua.learning.ui.actions.StatisticLearningHistoryAction
import vm.words.ua.learning.ui.states.StatisticLearningHistoryState


class StatisticLearningHistoryVm(
    private val manager: LearningHistoryManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        StatisticLearningHistoryState(
            toDate = Clock.System.now(),
        )
    )
    val state: StateFlow<StatisticLearningHistoryState> = mutableState

    init {
        fetch(state.value.toDate)
    }

    fun sent(action: StatisticLearningHistoryAction) {
        when (action) {
            is StatisticLearningHistoryAction.SetDate -> {
                fetch(action.date)
            }
        }
    }

    private fun fetch(date: Instant) {
        viewModelScope.launch(Dispatchers.Default) {
            val statistic = manager.getLearningHistoryStatistic(
                filter = StatisticsLearningHistoryFilter(
                    date = Range(
                        from = date.minus(STEP, kotlinx.datetime.DateTimeUnit.DAY, TimeZone.UTC),
                        to = date
                    )
                )
            ).content
            mutableState.value = mutableState.value.copy(statistic = statistic, toDate = date)
        }
    }

    companion object {
        internal const val STEP = 4L
    }
}