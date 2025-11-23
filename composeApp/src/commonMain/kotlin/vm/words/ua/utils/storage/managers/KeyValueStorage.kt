package vm.words.ua.utils.storage.managers

interface KeyValueStorage {
    fun putString(key: String, value: String?)
    fun getString(key: String, default: String? = null): String?

    fun putInt(key: String, value: Int)
    fun getInt(key: String, default: Int = 0): Int

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, default: Boolean = false): Boolean

    fun remove(key: String)
    fun clear()
}