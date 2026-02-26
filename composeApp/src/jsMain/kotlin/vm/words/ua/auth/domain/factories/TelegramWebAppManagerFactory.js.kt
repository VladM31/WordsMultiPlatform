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
}

actual fun createTelegramWebAppManager(): TelegramWebAppManager = TelegramWebAppManagerJs()

