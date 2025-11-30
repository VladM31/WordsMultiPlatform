package vm.words.ua.utils.validation.schemes


fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isEquals(vararg values: T): vm.words.ua.utils.validation.schemes.ValidationScheme<T> {
    if (values.isEmpty()) {
        return this
    }
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.EqualsAction(
            values.toList()
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isEquals(values: List<T>): vm.words.ua.utils.validation.schemes.ValidationScheme<T> {
    if (values.isEmpty()) {
        return this
    }
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.EqualsAction(
            values
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isNotEquals(vararg values: T): vm.words.ua.utils.validation.schemes.ValidationScheme<T> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.EqualsAction(
            values.toList(),
            false
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isNotEquals(values: List<T>): vm.words.ua.utils.validation.schemes.ValidationScheme<T> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.EqualsAction(
            values,
            false
        )
    )
}

fun <T : vm.words.ua.utils.validation.models.DifferentLanguageable> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isDifferentLanguage(): vm.words.ua.utils.validation.schemes.ValidationScheme<T> {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(_root_ide_package_.vm.words.ua.utils.validation.actions.DifferentLanguageAction())
}