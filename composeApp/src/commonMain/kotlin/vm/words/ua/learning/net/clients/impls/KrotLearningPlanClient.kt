package vm.words.ua.learning.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.learning.net.clients.LearningPlanClient
import vm.words.ua.learning.net.requests.LearningPlanRequest
import vm.words.ua.learning.net.responds.LearningPlanResponse

class KrotLearningPlanClient(
    private val client: HttpClient
) : LearningPlanClient {
    private val baseUrl: String by lazy {
        AppRemoteConfig.baseUrl + "/words-api/learning-plan"
    }

    override suspend fun getPlan(token: String): LearningPlanResponse? {
        val response = client.get(baseUrl) {
            headers { header("Authorization", "Bearer $token") }
        }
        if (response.contentLength() == 0L) {
            return null
        }

        // Пробуем десериализовать тело, ловим исключения
        return try {
            response.body<LearningPlanResponse?>()
        } catch (e: Exception) {
            println("Failed to parse body: $e")
            null
        }
    }

    override suspend fun createPlan(token: String, learningPlan: LearningPlanRequest): LearningPlanResponse {
        val response = client.post(baseUrl) {
            headers { header("Authorization", "Bearer $token") }
            setBody(learningPlan)
        }
        return response.body()
    }

    override suspend fun updatePlan(
        token: String,
        learningPlan: LearningPlanRequest
    ) {
        client.put(baseUrl) {
            headers { header("Authorization", "Bearer $token") }
            setBody(learningPlan)
        }

    }
}