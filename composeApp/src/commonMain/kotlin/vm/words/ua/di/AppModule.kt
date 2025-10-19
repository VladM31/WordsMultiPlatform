package vm.words.ua.di

import org.kodein.di.DI
import vm.words.ua.auth.di.authModules
import vm.words.ua.core.di.coreModule
import vm.words.ua.playlist.di.playlistModule


/**
 * Все модули приложения
 */
val appModules = DI {
    import(coreModule)
    import(authModules)
    import(playlistModule)
}

object DiContainer {
    val di = appModules
}
