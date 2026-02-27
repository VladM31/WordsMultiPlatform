@file:OptIn(ExperimentalWasmJsInterop::class)

package vm.words.ua.auth.domain.factories

import kotlinx.coroutines.await
import vm.words.ua.auth.domain.managers.ContactRequestStatus
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import kotlin.js.Promise

private class TelegramWebAppManagerWasm : TelegramWebAppManager {
    override val initData: String get() = getTgInitDataJs()
    override val userJson: String get() = getTgUserJsonJs()

    override suspend fun requestContact(): ContactRequestStatus {
        val result = requestTelegramContactJs().await<JsAny>()
        return when (jsAnyToString(result)) {
            "sent" -> ContactRequestStatus.Sent
            "cancelled" -> ContactRequestStatus.Cancelled
            else -> ContactRequestStatus.Unavailable
        }
    }

    override fun openLink(url: String): Boolean {
        return try {
            val res = openTelegramLinkJs(url)
            if (res) return true
            openWindowJs(url)
            true
        } catch (_: Throwable) {
            false
        }
    }
}

@JsFun("() => window.getTgInitData ? window.getTgInitData() : (window.TG_INIT_DATA || '')")
private external fun getTgInitDataJs(): String

@JsFun("() => window.getTgUserJson ? window.getTgUserJson() : (window.TG_USER_JSON || '')")
private external fun getTgUserJsonJs(): String

@JsFun("() => window.requestTelegramContact ? window.requestTelegramContact() : Promise.resolve('unavailable')")
private external fun requestTelegramContactJs(): Promise<JsAny>

@JsFun("(s) => typeof s === 'string' ? s : String(s)")
private external fun jsAnyToString(value: JsAny): String

@JsFun("(u) => { try { var tg = window.Telegram && window.Telegram.WebApp; if (tg && typeof tg.openTelegramLink === 'function') { tg.openTelegramLink(u); if (typeof tg.close === 'function') tg.close(); return true; } if (tg && typeof tg.openLink === 'function') { tg.openLink(u); if (typeof tg.close === 'function') tg.close(); return true; } return false; } catch(e) { console.warn('openTelegramLink error', e); return false; } }")
private external fun openTelegramLinkJs(url: String): Boolean

@JsFun("(u) => { try { window.open(u, '_blank'); return true; } catch(e){ return false; } }")
private external fun openWindowJs(url: String): Boolean

actual fun createTelegramWebAppManager(): TelegramWebAppManager = TelegramWebAppManagerWasm()
