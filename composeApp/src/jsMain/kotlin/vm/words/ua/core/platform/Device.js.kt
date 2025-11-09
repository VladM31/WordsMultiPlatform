package vm.words.ua.core.platform

actual fun getDeviceName(): String {
    val nav = js("(typeof navigator !== 'undefined' ? navigator : null)") as dynamic
    return try {
        val platform: String? = nav?.platform as? String
        val userAgent: String? = nav?.userAgent as? String
        val vendor: String? = nav?.vendor as? String

        val manufacturer = when {
            !vendor.isNullOrBlank() -> vendor
            !platform.isNullOrBlank() -> platform
            else -> "Browser"
        }
        val model = userAgent ?: platform ?: ""

        if (model.startsWith(manufacturer ?: "", ignoreCase = true)) {
            model.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        } else {
            "${manufacturer?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }} $model".trim()
        }
    } catch (e: Throwable) {
        "Browser"
    }
}
