package vm.words.ua.utils.validation.actions

actual fun domainToAscii(domain: String): String? {
    // No built-in IDN conversion on JS common without extra dependency.
    // Return null so common code falls back to original domain string.
    return null
}

