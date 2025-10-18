package vm.words.ua.auth.di

import org.koin.dsl.module


/**
 * Все модули приложения
 */
val authModules = module {
    includes(client)
}

