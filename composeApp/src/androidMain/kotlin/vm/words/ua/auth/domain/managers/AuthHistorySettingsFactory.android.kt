package vm.words.ua.auth.domain.managers

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

/**
 * Android implementation using SharedPreferences with encrypted node name
 */
actual object AuthHistorySettingsFactory {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    actual fun create(): Settings {
        val sharedPreferences = appContext.getSharedPreferences(
            "auth_history_encrypted",
            Context.MODE_PRIVATE
        )
        return SharedPreferencesSettings(sharedPreferences)
    }
}

