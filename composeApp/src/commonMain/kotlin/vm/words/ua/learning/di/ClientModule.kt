package vm.words.ua.learning.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.learning.net.clients.LearningHistoryClient
import vm.words.ua.learning.net.clients.LearningPlanClient
import vm.words.ua.learning.net.clients.impls.KrotLearningHistoryClient
import vm.words.ua.learning.net.clients.impls.KrotLearningPlanClient

internal val clientLearningModule = DI.Module("clientLearningModule") {
    // HTTP клиент (singleton)
    bind<LearningPlanClient>() with singleton {
        KrotLearningPlanClient(
            client = instance()
        )
    }

    bind<LearningHistoryClient>() with singleton {
        KrotLearningHistoryClient(
            client = instance()
        )
    }
}