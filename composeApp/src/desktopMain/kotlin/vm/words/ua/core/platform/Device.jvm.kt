package vm.words.ua.core.platform

import java.util.Locale

actual fun getDeviceName(): String {
    val os = System.getProperty("os.name") ?: "JVM"
    val arch = System.getProperty("os.arch") ?: ""
    val vendor = System.getProperty("java.vendor") ?: ""

    val manufacturer = vendor
    val model = os + if (arch.isNotBlank()) " ($arch)" else ""

    return if (model.startsWith(manufacturer, ignoreCase = true)) {
        model.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } else {
        "${manufacturer.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} $model".trim()
    }
}

