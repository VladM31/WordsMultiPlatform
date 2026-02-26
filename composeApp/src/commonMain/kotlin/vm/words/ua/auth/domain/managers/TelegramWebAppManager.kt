package vm.words.ua.auth.domain.managers

/**
 * Provides Telegram Mini App launch parameters injected by the browser
 * via window.TG_INIT_DATA / window.TG_USER_JSON.
 *
 * On non-web platforms both values are always empty strings.
 */
interface TelegramWebAppManager {
    /** Raw signed init-data string; send to backend for verification. */
    val initData: String

    /** JSON string of tg.initDataUnsafe (user, chat, etc.). */
    val userJson: String

    /** True when running inside a real Telegram Mini App context. */
    val isAvailable: Boolean get() = initData.isNotEmpty()

    /**
     * Shows a native Telegram dialog asking the user to share their phone number.
     * On success Telegram sends the contact to the bot server-side.
     * Requires Bot API 6.9+.
     */
    suspend fun requestContact(): ContactRequestStatus
}

sealed class ContactRequestStatus {
    /** User confirmed and the contact was sent to the bot. */
    data object Sent : ContactRequestStatus()

    /** User dismissed the dialog without sharing. */
    data object Cancelled : ContactRequestStatus()

    /** Not running inside a Telegram Mini App, or API not supported. */
    data object Unavailable : ContactRequestStatus()
}

