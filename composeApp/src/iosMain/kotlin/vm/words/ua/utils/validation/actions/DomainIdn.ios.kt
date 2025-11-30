package vm.words.ua.utils.validation.actions

actual fun domainToAscii(domain: String): String? {
    // Basic passthrough; iOS punycode conversion can be added if needed
    return domain
}

