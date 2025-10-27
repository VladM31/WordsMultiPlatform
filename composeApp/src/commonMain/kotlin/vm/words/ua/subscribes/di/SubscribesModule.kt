package vm.words.ua.subscribes.di

import org.kodein.di.DI

/**
 * Главный модуль Subscribe приложения
 */
val subscribesModule = DI.Module("subscribes") {
    import(subscribesClientModule)
    import(subscribesManagerModule)
}
