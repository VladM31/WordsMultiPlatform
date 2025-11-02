package vm.words.ua.exercise.net.clients.impls

import io.ktor.client.*
import io.ktor.client.request.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.exercise.net.clients.ExerciseStatisticalClient
import vm.words.ua.exercise.net.responds.EndExerciseTransactionRequest
import vm.words.ua.exercise.net.responds.StartExerciseTransactionRequest
import vm.words.ua.exercise.net.responds.WordCompletedRequest

class KrotExerciseStatisticalClient(
    private val client: HttpClient
) : ExerciseStatisticalClient {
    private val baseUrl: String by lazy {
        AppRemoteConfig.baseUrl + "/stat-api/statistical"
    }

    override suspend fun startExercise(
        request: StartExerciseTransactionRequest,
        vararg additionalHeaders: Pair<String, String>
    ) {
        executeRequest(request, "/start", additionalHeaders)
    }

    override suspend fun completeWord(request: WordCompletedRequest, vararg additionalHeaders: Pair<String, String>) {
        executeRequest(request, "/word-completed", additionalHeaders)
    }

    override suspend fun endExercise(
        request: EndExerciseTransactionRequest,
        vararg additionalHeaders: Pair<String, String>
    ) {
        executeRequest(request, "/end", additionalHeaders)
    }

    private suspend fun executeRequest(
        body: Any?,
        endpoint: String,
        additionalHeaders: Array<out Pair<String, String>>
    ) {

        client.post("$baseUrl$endpoint") {
            additionalHeaders.forEach {
                header(it.first, it.second)
            }
            setBody(body)
        }
    }
}