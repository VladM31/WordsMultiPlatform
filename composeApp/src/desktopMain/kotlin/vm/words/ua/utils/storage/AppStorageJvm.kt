package vm.words.ua.utils.storage

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.attribute.DosFileAttributeView
import java.util.*

actual fun createPlatformSettings(name: String): Settings {
    val currentDir = File(System.getProperty("user.dir"))

    val configDir = File(currentDir, "config")

    if (!configDir.exists()) {
        configDir.mkdirs()
    }
    println("Config directory: ${configDir.absolutePath}")

    try {
        val osName = System.getProperty("os.name")?.lowercase() ?: ""
        if ("windows" in osName) {
            val view = Files.getFileAttributeView(
                configDir.toPath(),
                DosFileAttributeView::class.java
            )
            view?.setHidden(true)
        }
    } catch (_: Throwable) {
    }

    val file = File(configDir, "$name.properties")

    val props = Properties()
    if (file.exists()) {
        FileInputStream(file).use { fis ->
            props.load(fis)
        }
    }

    val autoSavingProps = object : Properties() {
        override fun setProperty(key: String, value: String): Any? {
            val res = super.setProperty(key, value)
            saveToFile()
            return res
        }

        override fun remove(key: Any?): Any? {
            val res = super.remove(key)
            saveToFile()
            return res
        }

        override fun clear() {
            super.clear()
            saveToFile()
        }

        init {
            putAll(props)
        }

        private fun saveToFile() {
            try {
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                FileOutputStream(file).use { fos ->
                    store(fos, null)
                }
            } catch (_: Throwable) {
            }
        }
    }

    return PropertiesSettings(autoSavingProps)
}