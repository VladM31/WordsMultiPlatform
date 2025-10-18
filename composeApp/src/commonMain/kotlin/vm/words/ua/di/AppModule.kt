package vm.words.ua.di

import io.ktor.client.*
import org.koin.dsl.module
import vm.words.ua.auth.di.authModules
import vm.words.ua.auth.net.HttpClientFactory
import vm.words.ua.auth.net.clients.AuthClient
import vm.words.ua.auth.net.clients.KtorAuthClient
import vm.words.ua.core.domain.crypto.TokenCipherFactory
import vm.words.ua.core.domain.managers.SettingsFactory
import vm.words.ua.core.domain.managers.impl.SharedUserCacheManager
import vm.words.ua.core.domain.managers.UserCacheManager


/**
 * Модуль Koin для сетевых зависимостей
 */
val networkModule = module {
    // HTTP клиент (singleton)
    single<HttpClient> {
        HttpClientFactory.createAuthClient()
    }

    // Auth клиент (singleton)
    single<AuthClient> {
        KtorAuthClient(get())
    }
}

/**
 * Модуль Koin для кэш-менеджеров
 */
val cacheModule = module {
    single<UserCacheManager> {
        SharedUserCacheManager(
            settings = SettingsFactory.create(),
            tokenCipher = TokenCipherFactory.create()
        )
    }
}

/**
 * Все модули приложения
 */
val appModules = listOf(
    networkModule,
    cacheModule,
    authModules
)
