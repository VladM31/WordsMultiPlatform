package vm.words.ua.core.di

import org.kodein.di.DI


val coreModule = DI.Module("core") {
    import(networkCoreModule)
    import(managerCoreModule)
}