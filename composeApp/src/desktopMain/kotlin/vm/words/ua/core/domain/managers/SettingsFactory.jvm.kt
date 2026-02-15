package vm.words.ua.core.domain.managers

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences


actual object SettingsFactory {
    actual fun create(): Settings {
        val preferences = Preferences.userRoot().node("shared_user_cache_manager")
        return PreferencesSettings(preferences)
    }
}

