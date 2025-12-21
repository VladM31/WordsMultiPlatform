package vm.words.ua.core.analytics

/**
 * Android implementation - returns Firebase Analytics
 */
actual fun createAnalytics(): Analytics = FirebaseAnalytics()

