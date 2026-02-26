package vm.words.ua.auth.domain.factories

import vm.words.ua.auth.domain.managers.TelegramWebAppManager

private class TelegramWebAppManagerJs : TelegramWebAppManager {
    override val initData: String
        get() = js("window.getTgInitData ? window.getTgInitData() : (window.TG_INIT_DATA || '')") as? String ?: ""

    override val userJson: String
        get() = js("window.getTgUserJson ? window.getTgUserJson() : (window.TG_USER_JSON || '')") as? String ?: ""
}

actual fun createTelegramWebAppManager(): TelegramWebAppManager = TelegramWebAppManagerJs()

