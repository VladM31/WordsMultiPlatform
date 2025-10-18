package vm.words.ua.di

import org.koin.core.context.startKoin

private var koinStarted = false

/**
 * Initialize Koin DI once per process.
 */
fun initKoin() {
    if (!koinStarted) {
        startKoin {
            modules(appModules)
        }
        koinStarted = true
    }
}
