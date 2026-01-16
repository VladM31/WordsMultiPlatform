package vm.words.ua.learning.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.core.domain.models.Range
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.learning.domain.managers.LearningHistoryManager
import vm.words.ua.learning.domain.managers.LearningPlanManager
import vm.words.ua.learning.domain.models.CountLearningHistory
import vm.words.ua.learning.domain.models.LearningHistoryFilter
import vm.words.ua.learning.domain.models.LearningPlan
import vm.words.ua.learning.domain.models.enums.LearningHistoryType
import vm.words.ua.learning.domain.models.enums.PlanFragmentType
import vm.words.ua.learning.ui.actions.LearningPlanAction
import vm.words.ua.learning.ui.states.LearningPlanState

class LearningPlanVm(
    private val learningHistoryManager: LearningHistoryManager,
    private val learningPlanManager: LearningPlanManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(LearningPlanState())
    val state: StateFlow<LearningPlanState> = mutableState

    init {
        fetch()
    }

    fun sent(action: LearningPlanAction) {
        when (action) {
            is LearningPlanAction.ReFetchPlan -> fetch()
        }
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val plane = learningPlanManager.fetchLearningPlan()
                if (plane == null) {
                    mutableState.value = mutableState.value.copy(type = PlanFragmentType.CREATE)
                    return@launch
                }

                val learnedWordsToDay = toLearnedWordsToDay(plane)

                val countMap = learningHistoryManager.getCount().toMap()


                mutableState.value = state.value.copy(
                    type = PlanFragmentType.EDIT,
                    learningPlan = plane,
                    learnedWordsToDay = learnedWordsToDay,
                    addedWords = countMap[LearningHistoryType.CREATE] ?: 0,
                    learnedWords = countMap[LearningHistoryType.UPDATE] ?: 0
                )
            } catch (e: Exception) {
                mutableState.value =
                    mutableState.value.copy(errorMessage = ErrorMessage(message = e.message.orEmpty()))
            }
        }
    }

    private suspend fun toLearnedWordsToDay(plan: LearningPlan): Int {
        val now = Clock.System.now()
        val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val historyFilter = LearningHistoryFilter(date = Range.of(today));

        val typeGroup = learningHistoryManager.getLearningHistory(historyFilter).content.filter {
            it.cefr == plan.cefr && it.learningLang == plan.learningLang && it.nativeLang == plan.nativeLang
        }.groupBy { it.type }

        val addedToday = typeGroup[LearningHistoryType.CREATE] ?: return 0
        val learnedToday = typeGroup[LearningHistoryType.UPDATE]?.groupBy { it.wordId } ?: return 0

        return addedToday.count { learnedToday.containsKey(it.wordId) }
    }

    private fun PagedModels<CountLearningHistory>.toMap(): Map<LearningHistoryType, Int> {
        val map = mutableMapOf<LearningHistoryType, Int>()

        content.forEach { map[it.type] = it.count }

        return map
    }
}