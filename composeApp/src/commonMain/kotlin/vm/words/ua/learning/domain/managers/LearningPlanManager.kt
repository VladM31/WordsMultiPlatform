package vm.words.ua.learning.domain.managers

import vm.words.ua.learning.domain.models.LearningPlan

interface LearningPlanManager {

    suspend fun fetchLearningPlan(): LearningPlan?

    suspend fun createLearningPlan(learningPlan: LearningPlan): LearningPlan

    suspend fun updateLearningPlan(learningPlan: LearningPlan)
}