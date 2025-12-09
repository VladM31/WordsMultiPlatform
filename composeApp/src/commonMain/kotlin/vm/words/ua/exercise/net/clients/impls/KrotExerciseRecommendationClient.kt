package vm.words.ua.exercise.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.exercise.net.clients.ExerciseRecommendationClient
import vm.words.ua.exercise.net.requests.RecommendationRequest
import vm.words.ua.exercise.net.responds.CanRecommendExercisesRespond
import vm.words.ua.exercise.net.responds.ExerciseRecommendationRespond

class KrotExerciseRecommendationClient(
    private val client: HttpClient
) : ExerciseRecommendationClient {
    private val baseUrl: String by lazy {
        AppRemoteConfig.baseUrl + "/stat-api/recommendation/exercises"
    }


    override suspend fun recommendExercises(
        request: RecommendationRequest,
        vararg additionalHeaders: Pair<String, String>
    ): ExerciseRecommendationRespond {
        return client.get(baseUrl) {
            header("Content-Type", "application/json")
            additionalHeaders.forEach {
                header(it.first, it.second)
            }
            // Add wordIds as query parameters
            request.wordIds.forEach { wordId ->
                parameter("wordIds", wordId)
            }
        }.body()
    }

    override suspend fun canRecommendExercises(
        request: RecommendationRequest,
        vararg additionalHeaders: Pair<String, String>
    ): CanRecommendExercisesRespond {
        return client.get("${baseUrl}/can-recommend") {
            header("Content-Type", "application/json")
            additionalHeaders.forEach {
                header(it.first, it.second)
            }
            // Add wordIds as query parameters
            request.wordIds.forEach { wordId ->
                parameter("wordIds", wordId)
            }
        }.body()
    }
}