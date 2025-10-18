package vm.words.ua.di

private var diInitialized = false

/**
 * Initialize Kodein DI once per process.
 */
fun initDi() {
    if (!diInitialized) {
        DiContainer.di
        diInitialized = true
    }
}
