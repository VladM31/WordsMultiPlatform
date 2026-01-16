package vm.words.ua.learning.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import vm.words.ua.learning.ui.vms.LearningPlanVm
import vm.words.ua.learning.ui.vms.StatisticLearningHistoryVm

internal val learningViewModelDi = DI.Module("LearningViewModelDi") {
    bind<LearningPlanVm>() with provider {
        LearningPlanVm(
            learningHistoryManager = instance(),
            learningPlanManager = instance()
        )
    }

    bind<StatisticLearningHistoryVm>() with provider {
        StatisticLearningHistoryVm(
            manager = instance()
        )
    }
}