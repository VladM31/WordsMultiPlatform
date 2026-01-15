package vm.words.ua.learning.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.exercise.net.responds.StartExerciseTransactionRequest
import vm.words.ua.learning.domain.models.enums.PlanFragmentType

data class LearningPlanState(
    val type: PlanFragmentType = PlanFragmentType.UNDEFINED,
    val learningPlan: StartExerciseTransactionRequest.LearningPlan? = null,
    val learnedWordsToDay: Int = 1,
    val addedWords: Int = 0,
    val learnedWords: Int = 0,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState