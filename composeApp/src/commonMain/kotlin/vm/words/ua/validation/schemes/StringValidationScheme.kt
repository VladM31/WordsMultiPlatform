package vm.words.ua.validation.schemes

import vm.words.ua.validation.actions.EmailAction
import vm.words.ua.validation.actions.IsMatchesAction
import vm.words.ua.validation.actions.NotBlankAction
import vm.words.ua.validation.actions.NotEmptyAction
import vm.words.ua.validation.actions.StringLengthAction

fun ValidationScheme<String>.length(min: Int? = null, max: Int? = null) : ValidationScheme<String> {
    if (min == null && max == null) {
        return this
    }

    return add(StringLengthAction(min, max))
}

fun ValidationScheme<String>.notBlank(canBeEmpty: Boolean = false) : ValidationScheme<String> {
    return add(NotBlankAction(canBeEmpty))
}

fun ValidationScheme<String>.notEmpty() : ValidationScheme<String> {
    return add(NotEmptyAction)
}

fun ValidationScheme<String>.email(isRequired: Boolean = true) : ValidationScheme<String> {
    return add(EmailAction(isRequired))
}

fun ValidationScheme<String>.isMatches(regex: Regex) : ValidationScheme<String> {
    return add(IsMatchesAction(regex))
}

fun ValidationScheme<String>.isPhoneNumber() : ValidationScheme<String> {
    return add(IsMatchesAction(
        Regex("\\d{10,15}", RegexOption.IGNORE_CASE),
        message = "Value should be a valid phone number"
    ))
}

