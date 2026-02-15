package vm.words.ua.subscribes.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.subscribes.domain.managers.SubscribeCacheManager
import vm.words.ua.subscribes.domain.managers.impl.InMemorySubscribeCacheManager


internal val subscribesManagerModule = DI.Module("subscribesManager") {
    bind<SubscribeCacheManager>() with singleton {
        InMemorySubscribeCacheManager(
            subscribeClient = instance(),
            userCacheManager = instance()
        )
    }
}

