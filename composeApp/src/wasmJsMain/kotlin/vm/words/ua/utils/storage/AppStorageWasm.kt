package vm.words.ua.utils.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual fun createPlatformSettings(name: String): Settings = StorageSettings()