package vm.words.ua.core.domain.managers

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

/**
 * Android implementation using SharedPreferences
 */
actual object SettingsFactory {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    actual fun create(): Settings {
        val sharedPreferences = appContext.getSharedPreferences(
            "shared_user_cache_manager",
            Context.MODE_PRIVATE
        )
        return SharedPreferencesSettings(sharedPreferences)
    }
}

