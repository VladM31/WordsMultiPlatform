package vm.words.ua.auth.domain.managers.impls

import com.russhwolf.settings.Settings
import vm.words.ua.auth.domain.managers.AuthHistoryManager

/**
 * Multiplatform implementation of AuthHistoryManager using encrypted settings
 */
internal class AuthHistoryManagerImpl(
    private val settings: Settings
) : AuthHistoryManager {

    override val lastPhoneNumber: String?
        get() = settings.getStringOrNull(LAST_PHONE_NUMBER)

    override fun updateLastPhoneNumber(phoneNumber: String) {
        settings.putString(LAST_PHONE_NUMBER, phoneNumber)
    }

    companion object {
        private const val LAST_PHONE_NUMBER = "last_phone_number"
    }
}