package vm.words.ua.settings.di

import org.kodein.di.DI

val settingsModule = DI.Module("settingsModule") {
    import(viewModelSettingsModule)
}


