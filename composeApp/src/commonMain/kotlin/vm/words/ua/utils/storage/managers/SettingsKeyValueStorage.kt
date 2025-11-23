package vm.words.ua.utils.storage.managers

import com.russhwolf.settings.Settings
import vm.words.ua.utils.storage.crypto.CryptoEngine
import vm.words.ua.utils.storage.models.StorageSecurity

class SettingsKeyValueStorage(
    private val settings: Settings,
    private val storageName: String,
    private val security: StorageSecurity,
    private val cryptoEngine: CryptoEngine? = null,
) : KeyValueStorage {

    private fun scopedKey(key: String): String = "$storageName.$key"

    private fun wrap(value: String): String =
        if (security == StorageSecurity.SECURITY && cryptoEngine != null) {
            cryptoEngine.encrypt(value)
        } else {
            value
        }

    private fun unwrap(value: String): String =
        if (security == StorageSecurity.SECURITY && cryptoEngine != null) {
            try {
                cryptoEngine.decrypt(value)
            } catch (t: Throwable) {
                value
            }
        } else {
            value
        }


    override fun putString(key: String, value: String?) {
        val k = scopedKey(key)
        if (value == null) {
            settings.remove(k)
        } else {
            settings.putString(k, wrap(value))
        }
    }

    override fun getString(key: String, default: String?): String? {
        val k = scopedKey(key)
        val stored = settings.getStringOrNull(k) ?: return default
        return unwrap(stored)
    }


    override fun putInt(key: String, value: Int) {
        val k = scopedKey(key)
        settings.putInt(k, value)
    }

    override fun getInt(key: String, default: Int): Int {
        val k = scopedKey(key)
        return settings.getInt(k, default)
    }


    override fun putBoolean(key: String, value: Boolean) {
        val k = scopedKey(key)
        settings.putBoolean(k, value)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        val k = scopedKey(key)
        return settings.getBoolean(k, default)
    }

    override fun remove(key: String) {
        settings.remove(scopedKey(key))
    }

    override fun clear() {
        settings.clear()
    }
}