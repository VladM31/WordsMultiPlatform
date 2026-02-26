package vm.words.ua.auth.di

import org.kodein.di.DI


val authModules = DI.Module("auth") {
    import(client)
    import(managerDi)
    import(googleSignInDi)
    import(telegramWebAppDi)
    import(viewModelDi)

}
