package vm.words.ua.learning.ui.states

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.learning.domain.models.LearningPlan
import vm.words.ua.learning.domain.models.enums.PlanStatus

data class LearningPlanState(
    val type: PlanStatus = PlanStatus.UNDEFINED,
    val learningPlan: LearningPlan? = null,
    val detailsState: DetailsState? = null,
    val learnedWordsToDay: Int = 1,
    val addedWords: Int = 0,
    val learnedWords: Int = 0,
    val isLoading: Boolean = true,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState {


    data class DetailsState(
        val wordsPerDay: Int,
        val nativeLang: Language,
        val learningLang: Language,
        val cefr: CEFR
    )
}