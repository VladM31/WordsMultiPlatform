package vm.words.ua.learning.net.clients

import vm.words.ua.learning.net.requests.LearningPlanRequest
import vm.words.ua.learning.net.responds.LearningPlanResponse

interface LearningPlanClient {


    suspend fun getPlan(token: String): LearningPlanResponse?

    suspend fun createPlan(token: String, learningPlan: LearningPlanRequest): LearningPlanResponse

    suspend fun updatePlan(
        token: String,
        learningPlan: LearningPlanRequest
    )
}