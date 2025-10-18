package vm.words.ua.auth.di

import org.koin.dsl.module
import vm.words.ua.auth.domain.managers.AuthHistoryManager
import vm.words.ua.auth.domain.managers.AuthHistorySettingsFactory
import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.managers.impls.AuthHistoryManagerImpl
import vm.words.ua.auth.domain.managers.impls.AuthManagerImpl

internal val managerDi = module {
    // HTTP клиент (singleton)
    single<AuthManager> {
        AuthManagerImpl(
            authClient = get(),
            userCacheManager = get()
        )
    }

    single<AuthHistoryManager> {
        AuthHistoryManagerImpl(
            settings = AuthHistorySettingsFactory.create()
        )
    }
}