package vm.words.ua.navigation.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import vm.words.ua.navigation.SimpleNavController

val navigationModule = DI.Module("NavigationModule") {
    bind<SimpleNavController>() with singleton { SimpleNavController() }
}

