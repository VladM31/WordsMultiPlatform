package vm.words.ua.utils.net.di

import dev.jordond.connectivity.Connectivity
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import vm.words.ua.utils.net.createConnectivity

val newUtilsModule = DI.Module("NewUtilsModule") {
    bind<Connectivity>() with singleton { createConnectivity() }

    import(managersNetUtilsModule)
}