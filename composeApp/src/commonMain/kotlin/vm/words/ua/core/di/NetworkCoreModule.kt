package vm.words.ua.core.di

import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.net.HttpClientFactory
import vm.words.ua.core.net.client.FileApiClient
import vm.words.ua.core.net.client.RenderedPdfClient
import vm.words.ua.core.net.client.impls.KrotFileApiClient
import vm.words.ua.core.net.client.impls.KrotRenderedPdfClient

internal val networkCoreModule = DI.Module("networkCoreModule") {
    bind<HttpClient>() with singleton {
        HttpClientFactory.createHttpClient()
    }

    bind<FileApiClient>() with singleton {
        KrotFileApiClient(
            userCacheManager = instance<UserCacheManager>(),
            httpClient = instance<HttpClient>(),
            json = Json { ignoreUnknownKeys = true }
        )
    }

    bind<RenderedPdfClient>() with singleton {
        KrotRenderedPdfClient(
            httpClient = instance<HttpClient>()
        )
    }
}
