package vm.words.ua.learning.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import vm.words.ua.learning.ui.vms.LearningHistoryListViewModel
import vm.words.ua.learning.ui.vms.LearningPlanViewModel
import vm.words.ua.learning.ui.vms.StatisticLearningHistoryViewModel

internal val learningViewModelDi = DI.Module("LearningViewModelDi") {
    bind<LearningPlanViewModel>() with provider {
        LearningPlanViewModel(
            learningHistoryManager = instance(),
            learningPlanManager = instance()
        )
    }

    bind<StatisticLearningHistoryViewModel>() with provider {
        StatisticLearningHistoryViewModel(
            manager = instance()
        )
    }

    bind<LearningHistoryListViewModel>() with provider {
        LearningHistoryListViewModel(
            manager = instance()
        )
    }
}