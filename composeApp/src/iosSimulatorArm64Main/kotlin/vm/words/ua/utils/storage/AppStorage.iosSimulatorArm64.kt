package vm.words.ua.utils.storage

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual fun createPlatformSettings(name: String): Settings {
    // отдельное хранилище по имени
    val defaults = NSUserDefaults(suiteName = name) ?: NSUserDefaults.standardUserDefaults
    return NSUserDefaultsSettings(defaults)
}