package vm.words.ua.core.domain.managers

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

/**
 * JS implementation using localStorage
 */
actual object SettingsFactory {
    actual fun create(): Settings {
        return StorageSettings()
    }
}

