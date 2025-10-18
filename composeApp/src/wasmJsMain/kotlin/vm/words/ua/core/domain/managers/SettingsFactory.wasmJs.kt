package vm.words.ua.core.domain.managers

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

/**
 * WASM implementation using localStorage
 */
actual object SettingsFactory {
    actual fun create(): Settings {
        return StorageSettings()
    }
}

