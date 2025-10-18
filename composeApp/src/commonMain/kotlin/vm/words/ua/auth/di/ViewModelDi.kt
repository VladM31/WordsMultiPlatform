package vm.words.ua.auth.di

import org.koin.dsl.module
import vm.words.ua.auth.ui.vms.LoginViewModel

internal val viewModelDi = module {
    single <LoginViewModel> {
        LoginViewModel(
            authManager = get(),
            userCacheManager = get(),
            authHistoryManager = get(),
        )
    }
}