package vm.words.ua.learning.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.core.domain.models.Range
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.learning.domain.managers.LearningHistoryManager
import vm.words.ua.learning.domain.managers.LearningPlanManager
import vm.words.ua.learning.domain.models.CountLearningHistory
import vm.words.ua.learning.domain.models.LearningHistoryFilter
import vm.words.ua.learning.domain.models.LearningPlan
import vm.words.ua.learning.domain.models.enums.LearningHistoryType
import vm.words.ua.learning.domain.models.enums.PlanStatus
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
            is LearningPlanAction.ClearDetailsState -> {
                mutableState.update {
                    it.copy(detailsState = null)
                }
            }
            is LearningPlanAction.ReFetchPlan -> fetch()
            is LearningPlanAction.ModifyPlan -> handleModifyPlan()
            is LearningPlanAction.UpdateLearningLang -> {
                mutableState.update {
                    val detailsState = it.detailsState ?: return@update it
                    it.copy(
                        detailsState = detailsState.copy(learningLang = action.learningLang)
                    )
                }
            }

            is LearningPlanAction.UpdateNativeLang -> {
                mutableState.update {
                    val detailsState = it.detailsState ?: return@update it
                    it.copy(
                        detailsState = detailsState.copy(nativeLang = action.nativeLang)
                    )
                }
            }

            is LearningPlanAction.UpdateCefr -> {
                mutableState.update {
                    val detailsState = it.detailsState ?: return@update it
                    it.copy(
                        detailsState = detailsState.copy(cefr = action.cefr)
                    )
                }
            }

            is LearningPlanAction.IncreaseWordsPerDay -> {
                mutableState.update {
                    val detailsState = it.detailsState ?: return@update it
                    it.copy(
                        detailsState = detailsState.copy(wordsPerDay = detailsState.wordsPerDay + 1)
                    )
                }
            }

            is LearningPlanAction.DecreaseWordsPerDay -> {
                mutableState.update {
                    val detailsState = it.detailsState ?: return@update it
                    val newWordsPerDay = (detailsState.wordsPerDay - 1).coerceAtLeast(1)
                    it.copy(
                        detailsState = detailsState.copy(wordsPerDay = newWordsPerDay)
                    )
                }
            }

            is LearningPlanAction.SubmitPlan -> handleSubmitPlan()
        }
    }

    private fun handleSubmitPlan() {
        val details = state.value.detailsState ?: return

        val newPlan = LearningPlan(
            wordsPerDay = details.wordsPerDay,
            nativeLang = details.nativeLang,
            learningLang = details.learningLang,
            cefr = details.cefr,
            createdAt = Clock.System.now()
        )

        viewModelScope.launch(Dispatchers.Default) {
            try {
                if (state.value.type == PlanStatus.CREATE) {
                    learningPlanManager.createLearningPlan(newPlan)
                } else if (state.value.type == PlanStatus.EDIT) {
                    learningPlanManager.updateLearningPlan(newPlan)
                }
                fetch()
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    errorMessage = ErrorMessage(message = e.message.orEmpty()),
                    isLoading = false,
                    detailsState = null
                )
            }
        }

    }

    private fun handleModifyPlan() {
        val detailsState: LearningPlanState.DetailsState = LearningPlanState.DetailsState(
            nativeLang = state.value.learningPlan?.nativeLang ?: Language.UKRAINIAN,
            learningLang = state.value.learningPlan?.learningLang ?: Language.ENGLISH,
            cefr = state.value.learningPlan?.cefr ?: vm.words.ua.core.domain.models.enums.CEFR.A1,
            wordsPerDay = state.value.learningPlan?.wordsPerDay ?: 1
        )

        mutableState.update {
            it.copy(detailsState = detailsState)
        }
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val plane = learningPlanManager.fetchLearningPlan()
                if (plane == null) {
                    mutableState.value = mutableState.value.copy(
                        type = PlanStatus.CREATE,
                        isLoading = false,
                        detailsState = null
                    )
                    return@launch
                }

                val learnedWordsToDay = toLearnedWordsToDay(plane)
                val countMap = learningHistoryManager.getCount().toMap()

                mutableState.value = state.value.copy(
                    type = PlanStatus.EDIT,
                    learningPlan = plane,
                    learnedWordsToDay = learnedWordsToDay,
                    addedWords = countMap[LearningHistoryType.CREATE] ?: 0,
                    learnedWords = countMap[LearningHistoryType.UPDATE] ?: 0,
                    isLoading = false,
                    detailsState = null
                )
            } catch (e: Exception) {
                mutableState.value =
                    mutableState.value.copy(
                        errorMessage = ErrorMessage(message = e.message.orEmpty()),
                        isLoading = false
                    )
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