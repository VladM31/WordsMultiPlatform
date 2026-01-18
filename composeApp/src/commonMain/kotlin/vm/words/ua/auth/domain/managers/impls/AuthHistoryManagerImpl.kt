package vm.words.ua.auth.domain.managers.impls

import com.russhwolf.settings.Settings
import vm.words.ua.auth.domain.managers.AuthHistoryManager

/**
 * Multiplatform implementation of AuthHistoryManager using encrypted settings
 */
internal class AuthHistoryManagerImpl(
    private val settings: Settings
) : AuthHistoryManager {

    override val lastUsername: String?
        get() = settings.getStringOrNull(LAST_PHONE_NUMBER)

    override fun updateLastUsername(username: String) {
        settings.putString(LAST_PHONE_NUMBER, username)
    }

    companion object {
        private const val LAST_PHONE_NUMBER = "last_phone_number"
    }
}