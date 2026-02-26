@file:OptIn(ExperimentalWasmJsInterop::class)

package vm.words.ua.auth.domain.factories

import vm.words.ua.auth.domain.managers.TelegramWebAppManager

private class TelegramWebAppManagerWasm : TelegramWebAppManager {
    override val initData: String get() = getTgInitDataJs()
    override val userJson: String get() = getTgUserJsonJs()
}

@JsFun("() => window.getTgInitData ? window.getTgInitData() : (window.TG_INIT_DATA || '')")
private external fun getTgInitDataJs(): String

@JsFun("() => window.getTgUserJson ? window.getTgUserJson() : (window.TG_USER_JSON || '')")
private external fun getTgUserJsonJs(): String

actual fun createTelegramWebAppManager(): TelegramWebAppManager = TelegramWebAppManagerWasm()

