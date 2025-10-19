package vm.words.ua.validation.actions

import vm.words.ua.validation.models.ValidResult

internal class EmailAction(
    private val isRequired: Boolean = false
) : ValidAction<String> {

    // Local-part: unquoted or quoted string. Case-insensitive.
    private val LOCAL_PART_REGEX = Regex(
        """
        ^(?:
          [a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*
          |
          \"(?:\\[\x00-\x7F]|[^"\\])*\"
        )$
        """.trimIndent(),
        RegexOption.IGNORE_CASE
    )

    // Domain: hostname (labels with optional hyphens), IPv4, or IPv6 literal (basic)
    private val EMAIL_DOMAIN_REGEX = Regex(
        """
        ^(?:
          (?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\.)+[a-z]{2,}
          |
          (?:\d{1,3}\.){3}\d{1,3}
          |
          \[IPv6:[0-9a-f:]+\]
        )$
        """.trimIndent(),
        RegexOption.IGNORE_CASE
    )

    private val ERROR_RESULT = ValidResult(false, "Email is not valid")

    private fun isValidEmailLocalPart(localPart: String): Boolean {
        if (localPart.isEmpty() || localPart.length > MAX_LOCAL_PART_LENGTH) return false
        return LOCAL_PART_REGEX.matches(localPart)
    }

    override fun validate(value: String): ValidResult {
        if (value.isEmpty()) {
            return if (isRequired) ERROR_RESULT else ValidResult.valid()
        }

        if (value.isBlank()) return ERROR_RESULT

        val splitPosition = value.lastIndexOf('@')
        if (splitPosition <= 0 || splitPosition >= value.length - 1) return ERROR_RESULT

        val localPart = value.substring(0, splitPosition)
        val domainPart = value.substring(splitPosition + 1)

        if (!isValidEmailLocalPart(localPart)) return ERROR_RESULT

        return isValidDomainAddress(domainPart)
    }

    private fun isValidDomainAddress(domain: String): ValidResult {
        val trimmed = domain.trim()
        if (trimmed.isEmpty() || trimmed.endsWith(".")) return ERROR_RESULT

        // Try platform-specific ASCII conversion (punycode) if available
        val ascii = try {
            domainToAscii(trimmed)
        } catch (_: Throwable) {
            null
        }
        val toMatch = ascii ?: trimmed

        if (toMatch.length > 255) return ERROR_RESULT

        return if (EMAIL_DOMAIN_REGEX.matches(toMatch)) ValidResult.valid() else ERROR_RESULT
    }

    companion object {
        private const val MAX_LOCAL_PART_LENGTH = 64
    }
}