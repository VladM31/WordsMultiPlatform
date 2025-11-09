package vm.words.ua.core.platform

import kotlinx.browser.window

actual fun getDeviceName(): String {
    return try {
        val platform: String? = window.navigator.platform
        val userAgent: String? = window.navigator.userAgent
        val vendor: String? =
            if (window.navigator.asDynamic().vendor != undefined) window.navigator.asDynamic().vendor as? String else null

        val manufacturer = when {
            !vendor.isNullOrBlank() -> vendor
            !platform.isNullOrBlank() -> platform
            else -> "Browser"
        }
        val model = userAgent ?: platform ?: ""

        if (model.startsWith(manufacturer, ignoreCase = true)) {
            model.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        } else {
            "${manufacturer.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} $model".trim()
        }
    } catch (_: Throwable) {
        "Browser"
    }
}
