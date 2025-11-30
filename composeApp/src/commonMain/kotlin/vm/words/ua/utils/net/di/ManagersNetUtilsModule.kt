package vm.words.ua.utils.net.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.utils.net.managers.ConnectivityManager
import vm.words.ua.utils.net.managers.impls.JordondConnectivityManager

internal val managersNetUtilsModule = DI.Module("ManagersNetUtilsModule") {
    bind<ConnectivityManager>() with singleton {
        JordondConnectivityManager(
            connectivity = instance(),
            scope = instance("appScope")
        )
    }

}