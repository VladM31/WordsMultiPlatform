package vm.words.ua.learning.domain.managers.impls

import kotlinx.datetime.LocalDate
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.learning.domain.managers.LearningHistoryManager
import vm.words.ua.learning.domain.models.*
import vm.words.ua.learning.net.clients.LearningHistoryClient
import vm.words.ua.learning.net.responds.CountLearningHistoryResponse
import vm.words.ua.learning.net.responds.LearningHistoryResponse
import vm.words.ua.learning.net.responds.StatisticsLearningHistoryResponse

class LearningHistoryManagerImpl(
    private val client: LearningHistoryClient,
    private val userCacheManager: UserCacheManager
) : LearningHistoryManager {
    private val token: String
        get() = userCacheManager.token.value

    override suspend fun getLearningHistory(filter: LearningHistoryFilter): PagedModels<LearningHistory> {
        val query = filter.toQueryMap()
        val respond = client.getLearningHistory(token, query);
        return PagedModels.of(respond) { it.toModel() }
    }

    override suspend fun getLearningHistoryStatistic(filter: StatisticsLearningHistoryFilter): PagedModels<StatisticsLearningHistory> {
        val query = filter.toQueryMap()
        val respond = client.getLearningHistoryStatistic(token, query);

        return PagedModels.of(respond) { it.toModel() }
    }

    override suspend fun getCount(): PagedModels<CountLearningHistory> {
        return PagedModels.of(client.getCount(token)) { it.toModel() }
    }

    private fun LearningHistoryResponse.toModel(): LearningHistory {
        return LearningHistory(
            id = id,
            wordId = wordId,
            grade = grade,
            date = LocalDate.parse(date),
            type = type,
            original = original,
            nativeLang = nativeLang,
            learningLang = learningLang,
            cefr = cefr
        )
    }

    private fun StatisticsLearningHistoryResponse.toModel(): StatisticsLearningHistory {
        return StatisticsLearningHistory(
            count = count,
            grades = grades,
            date = LocalDate.parse(date),
            type = type
        )
    }

    private fun CountLearningHistoryResponse.toModel(): CountLearningHistory {
        return CountLearningHistory(
            count = count,
            type = type
        )
    }
}