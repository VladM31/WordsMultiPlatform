package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.auth.domain.managers.*
import vm.words.ua.auth.domain.managers.impls.AuthHistoryManagerImpl
import vm.words.ua.auth.domain.managers.impls.AuthManagerImpl
import vm.words.ua.auth.domain.managers.impls.GoogleAuthManagerImpl
import vm.words.ua.auth.domain.managers.impls.TelegramAuthManagerImpl

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

    bind<TelegramAuthManager>() with singleton {
        TelegramAuthManagerImpl(
            telegramAuthClient = instance(),
            userCacheManager = instance()
        )
    }

    bind<GoogleAuthManager>() with singleton {
        GoogleAuthManagerImpl(
            googleApiManager = instance(),
            googleAuthClient = instance(),
            userCacheManager = instance()
        )
    }
}