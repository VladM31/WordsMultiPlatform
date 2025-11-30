package vm.words.ua.utils.validation.schemes

fun vm.words.ua.utils.validation.schemes.ValidationScheme<String>.length(
    min: Int? = null,
    max: Int? = null
): vm.words.ua.utils.validation.schemes.ValidationScheme<String> {
    if (min == null && max == null) {
        return this
    }

    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.StringLengthAction(
            min,
            max
        )
    )
}

fun vm.words.ua.utils.validation.schemes.ValidationScheme<String>.notBlank(canBeEmpty: Boolean = false): vm.words.ua.utils.validation.schemes.ValidationScheme<String> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.NotBlankAction(
            canBeEmpty
        )
    )
}

fun vm.words.ua.utils.validation.schemes.ValidationScheme<String>.notEmpty(): vm.words.ua.utils.validation.schemes.ValidationScheme<String> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(_root_ide_package_.vm.words.ua.utils.validation.actions.NotEmptyAction)
}

fun vm.words.ua.utils.validation.schemes.ValidationScheme<String>.email(isRequired: Boolean = true): vm.words.ua.utils.validation.schemes.ValidationScheme<String> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.EmailAction(
            isRequired
        )
    )
}

fun vm.words.ua.utils.validation.schemes.ValidationScheme<String>.isMatches(regex: Regex): vm.words.ua.utils.validation.schemes.ValidationScheme<String> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.IsMatchesAction(
            regex
        )
    )
}

fun vm.words.ua.utils.validation.schemes.ValidationScheme<String>.isPhoneNumber(): vm.words.ua.utils.validation.schemes.ValidationScheme<String> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.IsMatchesAction(
            Regex("\\d{10,15}", RegexOption.IGNORE_CASE),
            message = "Value should be a valid phone number"
        )
    )
}

