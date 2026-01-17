package vm.words.ua.core.analytics

/**
 * WasmJS implementation - returns Firebase Analytics
 */
actual fun createAnalytics(): Analytics = FirebaseAnalytics()


