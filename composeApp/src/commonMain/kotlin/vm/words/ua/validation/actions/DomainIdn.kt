package vm.words.ua.validation.actions

/**
 * Convert a domain to its ASCII/punycode form when the platform supports it.
 * Platform-specific implementations should provide `actual`.
 */
expect fun domainToAscii(domain: String): String?
