package vm.words.ua.utils.validation.actions

import vm.words.ua.utils.validation.models.ValidResult

internal class EmailAction(
    private val isRequired: Boolean = false
) : ValidAction<String> {

    // Local-part: simplified pattern compatible with JS
    private val LOCAL_PART_REGEX = Regex(
        "[a-z0-9!#$%&'*+/=?^_`{|}~.-]+",
        RegexOption.IGNORE_CASE
    )

    // Domain: simplified pattern compatible with JS
    private val EMAIL_DOMAIN_REGEX = Regex(
        "[a-z0-9]([a-z0-9-]*[a-z0-9])?(\\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*",
        RegexOption.IGNORE_CASE
    )

    private val ERROR_RESULT =
        ValidResult(false, "Email is not valid")

    private fun isValidEmailLocalPart(localPart: String): Boolean {
        if (localPart.isEmpty() || localPart.length > _root_ide_package_.vm.words.ua.utils.validation.actions.EmailAction.Companion.MAX_LOCAL_PART_LENGTH) return false
        return LOCAL_PART_REGEX.matches(localPart)
    }

    override fun validate(value: String): vm.words.ua.utils.validation.models.ValidResult {
        if (value.isEmpty()) {
            return if (isRequired) ERROR_RESULT else _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
        }

        if (value.isBlank()) return ERROR_RESULT

        val splitPosition = value.trim().lastIndexOf('@')
        if (splitPosition <= 0 || splitPosition >= value.trim().length - 1) return ERROR_RESULT

        val localPart = value.trim().substring(0, splitPosition)
        val domainPart = value.trim().substring(splitPosition + 1)

        if (!isValidEmailLocalPart(localPart)) return ERROR_RESULT

        return isValidDomainAddress(domainPart)
    }

    private fun isValidDomainAddress(domain: String): vm.words.ua.utils.validation.models.ValidResult {
        val trimmed = domain.trim()
        if (trimmed.isEmpty() || trimmed.endsWith(".")) return ERROR_RESULT

        // Try platform-specific ASCII conversion (punycode) if available
        val ascii = try {
            _root_ide_package_.vm.words.ua.utils.validation.actions.domainToAscii(trimmed)
        } catch (_: Throwable) {
            null
        }
        val toMatch = ascii ?: trimmed

        if (toMatch.length > 255) return ERROR_RESULT

        return if (EMAIL_DOMAIN_REGEX.matches(toMatch)) _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid() else ERROR_RESULT
    }

    companion object {
        private const val MAX_LOCAL_PART_LENGTH = 64
    }
}