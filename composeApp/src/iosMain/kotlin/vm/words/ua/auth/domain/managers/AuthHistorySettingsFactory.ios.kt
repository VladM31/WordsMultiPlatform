package vm.words.ua.auth.domain.managers

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual object AuthHistorySettingsFactory {
    actual fun create(): Settings {
        val defaults = NSUserDefaults.standardUserDefaults()
        return NSUserDefaultsSettings(defaults)
    }
}
