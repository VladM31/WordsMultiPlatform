package vm.words.ua.words.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.words.net.clients.UserWordClient
import vm.words.ua.words.net.clients.WordClient
import vm.words.ua.words.net.clients.impls.KrotUserWordClient
import vm.words.ua.words.net.clients.impls.KrotWordClient

internal val clientWordsModule = DI.Module("clientWordsModule") {
    bind<WordClient>() with singleton {
        KrotWordClient(
            client = instance()
        )
    }

    bind<UserWordClient>() with singleton {
        KrotUserWordClient(
            client = instance()
        )
    }
}