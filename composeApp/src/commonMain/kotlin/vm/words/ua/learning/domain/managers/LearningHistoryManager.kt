package vm.words.ua.learning.domain.managers

import vm.words.ua.core.domain.models.PagedModels
import vm.words.ua.learning.domain.models.*

interface LearningHistoryManager {

    suspend fun getLearningHistory(filter: LearningHistoryFilter): PagedModels<LearningHistory>

    suspend fun getLearningHistoryStatistic(filter: StatisticsLearningHistoryFilter): PagedModels<StatisticsLearningHistory>

    suspend fun getCount(): PagedModels<CountLearningHistory>
}