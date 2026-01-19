package vm.words.ua.auth.di

import org.kodein.di.DI


/**
 * Все модули приложения
 */
val authModules = DI.Module("auth") {
    import(client)
    import(managerDi)
    import(googleSignInDi)
    import(viewModelDi)

}
