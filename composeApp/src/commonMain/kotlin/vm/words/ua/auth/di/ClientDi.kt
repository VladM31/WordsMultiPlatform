package vm.words.ua.auth.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import vm.words.ua.auth.net.HttpClientFactory
import vm.words.ua.auth.net.clients.AuthClient
import vm.words.ua.auth.net.clients.KtorAuthClient

/**
 * Модуль Koin для сетевых зависимостей
 */
internal val client = module {
    // HTTP клиент (singleton)
    single<HttpClient> {
        HttpClientFactory.createAuthClient()
    }

    // Auth клиент (singleton)
    single<AuthClient> {
        KtorAuthClient(get())
    }
}
