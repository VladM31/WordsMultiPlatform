package vm.words.ua.auth.domain.factories

import kotlinx.coroutines.await
import vm.words.ua.auth.domain.managers.ContactRequestStatus
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import kotlin.js.Promise

private class TelegramWebAppManagerJs : TelegramWebAppManager {
    override val initData: String
        get() = js("window.getTgInitData ? window.getTgInitData() : (window.TG_INIT_DATA || '')") as? String ?: ""

    override val userJson: String
        get() = js("window.getTgUserJson ? window.getTgUserJson() : (window.TG_USER_JSON || '')") as? String ?: ""

    override suspend fun requestContact(): ContactRequestStatus {
        val promise: Promise<String> = js(
            "window.requestTelegramContact ? window.requestTelegramContact() : Promise.resolve('unavailable')"
        ) as Promise<String>
        return when (promise.await()) {
            "sent" -> ContactRequestStatus.Sent
            "cancelled" -> ContactRequestStatus.Cancelled
            else -> ContactRequestStatus.Unavailable
        }
    }

    override fun openLink(url: String): Boolean {
        // Try Telegram WebApp API first
        val res =
            js("(function(u){ try { var tg = window.Telegram && window.Telegram.WebApp; if (tg && typeof tg.openTelegramLink === 'function') { tg.openTelegramLink(u); if (typeof tg.close === 'function') tg.close(); return true; } if (tg && typeof tg.openLink === 'function') { tg.openLink(u); if (typeof tg.close === 'function') tg.close(); return true; } return false; } catch(e){ console.warn('openTelegramLink error', e); return false;} })(arguments[0])") as? Boolean
        if (res == true) return true
        // Fallback to opening in a new tab/window
        try {
            js("window.open(arguments[0], '_blank')")(url)
            return true
        } catch (_: Throwable) {
            return false
        }
    }
}

actual fun createTelegramWebAppManager(): TelegramWebAppManager = TelegramWebAppManagerJs()
