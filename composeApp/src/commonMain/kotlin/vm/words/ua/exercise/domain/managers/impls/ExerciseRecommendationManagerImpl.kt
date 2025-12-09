package vm.words.ua.exercise.domain.managers.impls

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.utils.toPair
import vm.words.ua.exercise.domain.managers.ExerciseRecommendationManager
import vm.words.ua.exercise.domain.models.data.ExerciseRecommendation
import vm.words.ua.exercise.domain.models.data.RecommendationRequest
import vm.words.ua.exercise.net.clients.ExerciseRecommendationClient
import vm.words.ua.exercise.net.responds.ExerciseRecommendationRespond

class ExerciseRecommendationManagerImpl(
    private val client: ExerciseRecommendationClient,
    private val userManager: UserCacheManager
) : ExerciseRecommendationManager {

    override suspend fun recommendExercises(request: RecommendationRequest): ExerciseRecommendation? {
        return try {
            val response = client.recommendExercises(
                request = request.toNetworkRequest(),
                userManager.toPair()
            )
            response.toDomainModel()
        } catch (e: Exception) {
            println("ExerciseRecommendationManager - Failed to recommend exercises: ${e.message}")
            null
        }
    }

    override suspend fun canRecommendExercises(request: RecommendationRequest): Boolean {
        return try {
            val response = client.canRecommendExercises(
                request = request.toNetworkRequest(),
                userManager.toPair()
            )
            response.canRecommend
        } catch (e: Exception) {
            println("ExerciseRecommendationManager - Failed to check if can recommend exercises: ${e.message}")
            false
        }
    }

    private fun RecommendationRequest.toNetworkRequest(): vm.words.ua.exercise.net.requests.RecommendationRequest {
        return vm.words.ua.exercise.net.requests.RecommendationRequest(
            wordIds = this.wordIds
        )
    }

    private fun ExerciseRecommendationRespond.toDomainModel(): ExerciseRecommendation {
        return ExerciseRecommendation(
            recommendationId = this.recommendationId,
            exercises = this.exercises
        )
    }
}
