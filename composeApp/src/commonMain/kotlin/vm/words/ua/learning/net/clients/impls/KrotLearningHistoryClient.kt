package vm.words.ua.learning.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.headers
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.learning.net.clients.LearningHistoryClient
import vm.words.ua.learning.net.responds.CountLearningHistoryResponse
import vm.words.ua.learning.net.responds.LearningHistoryResponse
import vm.words.ua.learning.net.responds.StatisticsLearningHistoryResponse

class KrotLearningHistoryClient(
    private val client: HttpClient
) : LearningHistoryClient {
    private val baseUrl: String by lazy {
        AppRemoteConfig.baseUrl + "/words-api/learning-history"
    }

    override suspend fun getLearningHistory(
        token: String,
        query: Map<String, String>
    ): PagedRespond<LearningHistoryResponse> {
        return client.get(baseUrl) {
            headers { header("Authorization", "Bearer $token") }
            query.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }

    override suspend fun getLearningHistoryStatistic(
        token: String,
        query: Map<String, String>
    ): PagedRespond<StatisticsLearningHistoryResponse> {
        return client.get("$baseUrl/statistics") {
            headers { header("Authorization", "Bearer $token") }
            query.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }

    override suspend fun getCount(token: String): PagedRespond<CountLearningHistoryResponse> {
        return client.get("$baseUrl/count") {
            headers { header("Authorization", "Bearer $token") }
        }.body()
    }
}