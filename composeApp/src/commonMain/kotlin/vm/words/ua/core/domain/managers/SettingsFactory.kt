package vm.words.ua.core.domain.managers

import com.russhwolf.settings.Settings

/**
 * Factory to create platform-specific Settings instance
 */
expect object SettingsFactory {
    fun create(): Settings
}

