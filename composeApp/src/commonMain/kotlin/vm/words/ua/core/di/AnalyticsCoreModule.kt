package vm.words.ua.core.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.createAnalytics

/**
 * DI module for Analytics
 */
val analyticsCoreModule = DI.Module("analyticsCore") {
    bindSingleton<Analytics> { createAnalytics() }
}

