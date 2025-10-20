package vm.words.ua.validation.actions

import java.net.IDN

actual fun domainToAscii(domain: String): String? {
    return try {
        val ascii = IDN.toASCII(domain)
        // IDN.toASCII may return an empty string for invalid input
        if (ascii.isNullOrBlank()) null else ascii
    } catch (_: IllegalArgumentException) {
        null
    }
}

