package vm.words.ua.learning.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.learning.domain.managers.LearningHistoryManager
import vm.words.ua.learning.domain.managers.impls.LearningHistoryManagerImpl

internal val learningManagerDi = DI.Module("LearningManagerDi") {
    bind<LearningHistoryManager>() with singleton {
        LearningHistoryManagerImpl(
            client = instance(),
            userCacheManager = instance()
        )
    }
}