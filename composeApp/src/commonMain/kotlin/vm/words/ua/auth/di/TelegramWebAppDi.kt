package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import vm.words.ua.auth.domain.factories.createTelegramWebAppManager
import vm.words.ua.auth.domain.managers.TelegramWebAppManager

val telegramWebAppDi = DI.Module("telegramWebApp") {
    bindSingleton<TelegramWebAppManager> { createTelegramWebAppManager() }
}

