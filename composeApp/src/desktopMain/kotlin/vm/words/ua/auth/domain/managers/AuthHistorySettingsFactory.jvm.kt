package vm.words.ua.auth.domain.managers

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences


actual object AuthHistorySettingsFactory {
    actual fun create(): Settings {
        val preferences = Preferences.userRoot().node("auth_history_encrypted")
        return PreferencesSettings(preferences)
    }
}

