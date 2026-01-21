package vm.words.ua.settings.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.settings.ui.vm.InstructionViewModel
import vm.words.ua.settings.ui.vm.PolicyViewModel
import vm.words.ua.settings.ui.vm.ProfileViewModel

internal val viewModelSettingsModule = DI.Module("viewModelSettingsModule") {
    bind<PolicyViewModel>() with factory {
        PolicyViewModel(
            byteContentManager = instance()
        )
    }

    bind<InstructionViewModel>() with factory {
        InstructionViewModel(
            byteContentManager = instance()
        )
    }

    bind<ProfileViewModel>() with factory {
        ProfileViewModel(
            userCacheManager = instance(),
            userManager = instance()
        )
    }
}