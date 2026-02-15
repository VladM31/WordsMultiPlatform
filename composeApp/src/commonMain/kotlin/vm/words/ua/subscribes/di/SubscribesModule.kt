package vm.words.ua.subscribes.di

import org.kodein.di.DI


val subscribesModule = DI.Module("subscribes") {
    import(subscribesClientModule)
    import(subscribesManagerModule)
}
