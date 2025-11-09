package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.auth.ui.vms.SignUpViewModel

internal val viewModelDi = DI.Module("viewModel") {
    bind<LoginViewModel>() with factory {
        LoginViewModel(
            authManager = instance(),
            authHistoryManager = instance(),
        )
    }

    bind<SignUpViewModel>() with factory {
        SignUpViewModel(
            authManager = instance()
        )
    }
}