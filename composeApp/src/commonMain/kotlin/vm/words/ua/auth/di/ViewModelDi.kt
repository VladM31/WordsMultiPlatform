package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.auth.ui.vms.ConfirmSignUpVm
import vm.words.ua.auth.ui.vms.LoginViewModel
import vm.words.ua.auth.ui.vms.SignUpViewModel
import vm.words.ua.auth.ui.vms.TelegramLoginVm

internal val viewModelDi = DI.Module("viewModel") {
    bind<LoginViewModel>() with factory {
        LoginViewModel(
            authManager = instance(),
            authHistoryManager = instance(),
            analytics = instance(),
            googleApiManager = instance(),
            googleAuthManager = instance(),
        )
    }

    bind<SignUpViewModel>() with factory {
        SignUpViewModel(
            authManager = instance(),
            analytics = instance()
        )
    }

    bind<ConfirmSignUpVm>() with factory {
        ConfirmSignUpVm(
            authManager = instance()
        )
    }

    bind<TelegramLoginVm>() with factory {
        TelegramLoginVm(
            telegramAuthManager = instance(),
            authHistoryManager = instance(),
            analytics = instance()
        )
    }
}