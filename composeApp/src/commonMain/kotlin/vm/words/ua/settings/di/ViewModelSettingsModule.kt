package vm.words.ua.settings.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.settings.ui.vm.PolicyViewModel

internal val viewModelSettingsModule = DI.Module("viewModelSettingsModule") {
    bind<PolicyViewModel>() with factory {
        PolicyViewModel(
            byteContentManager = instance()
        )
    }
}