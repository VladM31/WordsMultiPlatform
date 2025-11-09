package vm.words.ua.core.platform

@OptIn(kotlin.js.ExperimentalJsExport::class)
actual fun getDeviceName(): String {
    val nav = try {
        js("(typeof navigator !== 'undefined' ? navigator : null)") as dynamic
    } catch (e: Throwable) {
        null
    }

    return try {
        val platform: String? = nav?.platform as? String
        val userAgent: String? = nav?.userAgent as? String
        val vendor: String? = nav?.vendor as? String

        val manufacturer = when {
            !vendor.isNullOrBlank() -> vendor
            !platform.isNullOrBlank() -> platform
            else -> "WASM"
        }
        val model = userAgent ?: platform ?: ""

        if (model.startsWith(manufacturer ?: "", ignoreCase = true)) {
            model.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        } else {
            "${manufacturer?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} $model".trim()
        }
    } catch (e: Throwable) {
        "WASM"
    }
}
