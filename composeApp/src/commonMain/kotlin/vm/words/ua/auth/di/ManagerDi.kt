package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.auth.domain.managers.AuthHistoryManager
import vm.words.ua.auth.domain.managers.AuthHistorySettingsFactory
import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.managers.impls.AuthHistoryManagerImpl
import vm.words.ua.auth.domain.managers.impls.AuthManagerImpl

internal val managerDi = DI.Module("manager") {
    // HTTP клиент (singleton)
    bind<AuthManager>() with singleton {
        AuthManagerImpl(
            authClient = instance(),
            userCacheManager = instance()
        )
    }

    bind<AuthHistoryManager>() with singleton {
        AuthHistoryManagerImpl(
            settings = AuthHistorySettingsFactory.create()
        )
    }
}