package vm.words.ua.utils.storage

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

object AndroidStorageConfig {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}

actual fun createPlatformSettings(name: String): Settings {
    val context: Context = AndroidStorageConfig.appContext
    val prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    return SharedPreferencesSettings(prefs)
}