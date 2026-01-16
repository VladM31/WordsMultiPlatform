package vm.words.ua.learning.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.learning.ui.vms.LearningPlanVm

internal val learningViewModelDi = DI.Module("LearningViewModelDi") {
    bind<LearningPlanVm>() with singleton {
        LearningPlanVm(
            learningHistoryManager = instance(),
            learningPlanManager = instance()
        )
    }
}