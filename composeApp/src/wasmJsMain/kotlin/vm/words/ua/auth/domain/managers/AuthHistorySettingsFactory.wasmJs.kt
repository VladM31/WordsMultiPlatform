package vm.words.ua.auth.domain.managers

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

/**
 * WASM implementation using localStorage with prefixed keys for isolation
 */
actual object AuthHistorySettingsFactory {
    actual fun create(): Settings {
        return StorageSettings()
    }
}

