package vm.words.ua.core.di

import io.ktor.client.HttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import vm.words.ua.auth.net.HttpClientFactory

/**
 * Модуль Kodein-DI для сетевых зависимостей
 */
internal val networkCoreModule = DI.Module("networkCoreModule") {
    // HTTP клиент (singleton)
    bind<HttpClient>() with singleton {
        HttpClientFactory.createAuthClient()
    }
}
