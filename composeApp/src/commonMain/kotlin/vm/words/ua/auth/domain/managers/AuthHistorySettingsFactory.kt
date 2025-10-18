package vm.words.ua.auth.domain.managers

import com.russhwolf.settings.Settings

/**
 * Factory to create encrypted Settings instance for auth history
 */
expect object AuthHistorySettingsFactory {
    fun create(): Settings
}

