package vm.words.ua.subscribes.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.subscribes.net.clients.PayClient
import vm.words.ua.subscribes.net.clients.SubscribeClient
import vm.words.ua.subscribes.net.clients.impl.KtorPayClient
import vm.words.ua.subscribes.net.clients.impl.KtorSubscribeClient


internal val subscribesClientModule = DI.Module("subscribesClient") {
    bind<SubscribeClient>() with singleton {
        KtorSubscribeClient(instance())
    }

    bind<PayClient>() with singleton {
        KtorPayClient(instance(), instance())
    }
}
