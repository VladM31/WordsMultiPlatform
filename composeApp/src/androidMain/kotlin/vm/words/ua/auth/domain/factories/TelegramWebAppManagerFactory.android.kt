package vm.words.ua.auth.domain.factories

import vm.words.ua.auth.domain.managers.TelegramWebAppManager

private object EmptyTelegramWebAppManager : TelegramWebAppManager {
    override val initData: String = ""
    override val userJson: String = ""
}

actual fun createTelegramWebAppManager(): TelegramWebAppManager = EmptyTelegramWebAppManager

