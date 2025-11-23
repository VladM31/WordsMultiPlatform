package vm.words.ua.utils.storage

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

actual fun createPlatformSettings(name: String): Settings {
    val prefsNode: Preferences = Preferences.userRoot().node(name)
    return PreferencesSettings(prefsNode)
}