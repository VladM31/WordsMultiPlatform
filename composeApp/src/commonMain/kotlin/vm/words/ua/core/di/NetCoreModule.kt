package vm.words.ua.core.di

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.net.client.FileApiClient
import vm.words.ua.core.net.client.impls.KrotFileApiClient

/**
 * DI module for core network clients
 */
internal val netCoreModule = DI.Module("netCoreModule") {
    bind<FileApiClient>() with singleton {
        KrotFileApiClient(
            userCacheManager = instance<UserCacheManager>(),
            httpClient = instance<HttpClient>(),
            json = Json { ignoreUnknownKeys = true }
        )
    }
}

