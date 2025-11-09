package vm.words.ua.core.platform

/**
 * Return user-visible device name, e.g. "Samsung Galaxy S10" or "MacBook Pro".
 * Platform-specific implementations should produce a readable name and attempt
 * to capitalise manufacturer correctly.
 */
expect fun getDeviceName(): String
