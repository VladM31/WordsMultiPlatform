package vm.words.ua.core.analytics

/**
 * iOS implementation - returns Firebase Analytics
 */
actual fun createAnalytics(): Analytics = FirebaseAnalytics()

