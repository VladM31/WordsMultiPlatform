package vm.words.ua.di

import io.ktor.client.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.auth.di.authModules
import vm.words.ua.auth.net.HttpClientFactory
import vm.words.ua.core.domain.crypto.TokenCipherFactory
import vm.words.ua.core.domain.managers.SettingsFactory
import vm.words.ua.core.domain.managers.impl.SharedUserCacheManager
import vm.words.ua.core.domain.managers.UserCacheManager


/**
 * Модуль Kodein-DI для сетевых зависимостей
 */
val networkModule = DI.Module("network") {
    // HTTP клиент (singleton)
    bind<HttpClient>() with singleton {
        HttpClientFactory.createAuthClient()
    }
}

/**
 * Модуль Kodein-DI для кэш-менеджеров
 */
val cacheModule = DI.Module("cache") {
    bind<UserCacheManager>() with singleton {
        SharedUserCacheManager(
            settings = SettingsFactory.create(),
            tokenCipher = TokenCipherFactory.create()
        )
    }
}

/**
 * Все модули приложения
 */
val appModules = DI {
    import(networkModule)
    import(cacheModule)
    import(authModules)
}

object DiContainer {
    val di = appModules
}
