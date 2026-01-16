package vm.words.ua.learning.ui.actions

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

interface LearningPlanAction {

    data object ReFetchPlan : LearningPlanAction
    data object ModifyPlan : LearningPlanAction
    data object SubmitPlan : LearningPlanAction

    data object IncreaseWordsPerDay : LearningPlanAction
    data object DecreaseWordsPerDay : LearningPlanAction
    data object ClearDetailsState : LearningPlanAction

    data class UpdateCefr(val cefr: CEFR) : LearningPlanAction
    data class UpdateLearningLang(val learningLang: Language) : LearningPlanAction
    data class UpdateNativeLang(val nativeLang: Language) : LearningPlanAction



}