package vm.words.ua.core.domain.managers

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual object SettingsFactory {
    actual fun create(): Settings {
        val defaults = NSUserDefaults.standardUserDefaults()
        return NSUserDefaultsSettings(defaults)
    }
}
