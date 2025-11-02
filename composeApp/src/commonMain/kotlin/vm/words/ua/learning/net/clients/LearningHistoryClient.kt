package vm.words.ua.learning.net.clients

import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.learning.net.responds.CountLearningHistoryResponse
import vm.words.ua.learning.net.responds.LearningHistoryResponse
import vm.words.ua.learning.net.responds.StatisticsLearningHistoryResponse

interface LearningHistoryClient {

    suspend fun getLearningHistory(
        token: String,
        query: Map<String, String>
    ): PagedRespond<LearningHistoryResponse>


    suspend fun getLearningHistoryStatistic(
        token: String,
        query: Map<String, String>,
    ): PagedRespond<StatisticsLearningHistoryResponse>


    suspend fun getCount(
        token: String
    ): PagedRespond<CountLearningHistoryResponse>
}