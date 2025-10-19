package vm.words.ua.playlist.di

import io.ktor.client.HttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.playlist.net.clients.PinPlayListClient
import vm.words.ua.playlist.net.clients.PlayListClient
import vm.words.ua.playlist.net.clients.impls.KtorPinPlayListClient
import vm.words.ua.playlist.net.clients.impls.KtorPlayListClient


internal val clientsPlaylistModule = DI.Module("clientsPlaylistModule"){
    bind<PlayListClient>() with singleton {
        KtorPlayListClient(instance<HttpClient>())
    }

    bind<PinPlayListClient>() with singleton {
        KtorPinPlayListClient(instance<HttpClient>())
    }
}