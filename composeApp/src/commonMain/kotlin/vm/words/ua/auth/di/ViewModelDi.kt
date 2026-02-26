package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.auth.ui.vms.*

internal val viewModelDi = DI.Module("viewModel") {
    bind<LoginViewModel>() with factory {
        LoginViewModel(
            authManager = instance(),
            authHistoryManager = instance(),
            analytics = instance(),
            googleApiManager = instance(),
            googleAuthManager = instance(),
            telegramAuthManager = instance(),
            telegramWebAppManager = instance()
        )
    }

    bind<TelegramSignUpViewModel>() with factory {
        TelegramSignUpViewModel(
            authManager = instance(),
            analytics = instance(),
            telegramWebAppManager = instance(),
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
            analytics = instance(),
            telegramWebAppManager = instance()
        )
    }

    bind<GoogleSignUpViewModel>() with factory {
        GoogleSignUpViewModel(
            googleAuthManager = instance(),
            analytics = instance()
        )
    }
}