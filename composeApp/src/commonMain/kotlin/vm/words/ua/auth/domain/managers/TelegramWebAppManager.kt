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
}

