package vm.words.ua.di

import io.ktor.client.*
import org.koin.dsl.module
import vm.words.ua.auth.di.authModules
import vm.words.ua.auth.net.HttpClientFactory


private val  initModule = module {
    single<HttpClient> {
        HttpClientFactory.createAuthClient()
    }

}

/**
 * Все модули приложения
 */
val appModules = listOf(
    initModule,
    authModules
)

