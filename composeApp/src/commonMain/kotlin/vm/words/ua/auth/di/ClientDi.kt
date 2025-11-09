package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.auth.net.clients.AuthClient
import vm.words.ua.auth.net.clients.TelegramAuthClient
import vm.words.ua.auth.net.clients.impls.KrotTelegramAuthClient
import vm.words.ua.auth.net.clients.impls.KtorAuthClient

/**
 * Модуль Kodein-DI для сетевых зависимостей
 */
internal val client = DI.Module("client") {
    // Auth клиент (singleton)
    bind<AuthClient>() with singleton {
        KtorAuthClient(instance())
    }

    bind<TelegramAuthClient>() with singleton {
        KrotTelegramAuthClient(httpClient = instance())
    }
}
