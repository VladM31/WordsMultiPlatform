package vm.words.ua.learning.domain.managers.impls

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.learning.domain.managers.LearningPlanManager
import vm.words.ua.learning.domain.models.LearningPlan
import vm.words.ua.learning.net.clients.LearningPlanClient
import vm.words.ua.learning.net.requests.LearningPlanRequest
import vm.words.ua.learning.net.responds.LearningPlanResponse


class LearningPlanManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val learningPlanClient: LearningPlanClient
) : LearningPlanManager {
    private val token: String
        get() = userCacheManager.token.value

    override suspend fun fetchLearningPlan(): LearningPlan? {
        return try {
            learningPlanClient.getPlan(token)?.toModel()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createLearningPlan(learningPlan: LearningPlan): LearningPlan {
        return learningPlanClient.createPlan(token, learningPlan.toRequest()).toModel()
    }

    override suspend fun updateLearningPlan(learningPlan: LearningPlan) {
        learningPlanClient.updatePlan(token, learningPlan.toRequest())
    }

    private fun LearningPlanResponse.toModel(): LearningPlan {
        return LearningPlan(
            wordsPerDay = wordsPerDay,
            nativeLang = nativeLang,
            learningLang = learningLang,
            cefr = cefr,
            createdAt = createdAt
        )
    }

    private fun LearningPlan.toRequest(): LearningPlanRequest {
        return LearningPlanRequest(
            wordsPerDay = wordsPerDay,
            nativeLang = nativeLang,
            learningLang = learningLang,
            cefr = cefr
        )
    }
}