package vm.words.ua.utils.storage


import com.russhwolf.settings.Settings
import vm.words.ua.utils.storage.crypto.XorCryptoEngine
import vm.words.ua.utils.storage.managers.KeyValueStorage
import vm.words.ua.utils.storage.managers.SettingsKeyValueStorage
import vm.words.ua.utils.storage.models.StorageSecurity

/**
 * Платформенная реализация создания Settings по имени хранилища.
 */
expect fun createPlatformSettings(name: String): Settings

object AppStorage {

    /**
     * Создаёт KMP-аналог SharedPreferences / NSUserDefaults / localStorage и т.п.
     *
     * @param name        имя хранилища (аналог имени SharedPreferences)
     * @param security    NON_SECURITY / SECURITY
     * @param cryptoKey   ключ для XOR-шифрования (по умолчанию = name)
     */
    fun create(
        name: String,
        security: StorageSecurity = StorageSecurity.NON_SECURITY,
        cryptoKey: String = name,
    ): KeyValueStorage {
        val settings = createPlatformSettings(name)
        val crypto = if (security == StorageSecurity.SECURITY) {
            XorCryptoEngine(cryptoKey)
        } else {
            null
        }

        return SettingsKeyValueStorage(
            settings = settings,
            storageName = name,
            security = security,
            cryptoEngine = crypto,
        )
    }
}
