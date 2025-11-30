package vm.words.ua.utils.validation.schemes


fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isGreaterThan(value: T): vm.words.ua.utils.validation.schemes.ValidationScheme<T> where T : Comparable<T>, T : Number {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.NumberRangeAction(
            { o1, o2 -> o1.compareTo(o2) },
            min = value
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isGreaterThanOrEquals(value: T): vm.words.ua.utils.validation.schemes.ValidationScheme<T> where T : Comparable<T>, T : Number {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.NumberRangeAction(
            { o1, o2 -> o1.compareTo(o2) },
            min = value,
            minInclusive = true
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isLessThan(value: T): vm.words.ua.utils.validation.schemes.ValidationScheme<T> where T : Comparable<T>, T : Number {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.NumberRangeAction(
            { o1, o2 -> o1.compareTo(o2) },
            max = value
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isLessThanOrEquals(value: T): vm.words.ua.utils.validation.schemes.ValidationScheme<T> where T : Comparable<T>, T : Number {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.NumberRangeAction(
            { o1, o2 -> o1.compareTo(o2) },
            max = value,
            maxInclusive = true
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isBetween(
    min: T,
    max: T
): vm.words.ua.utils.validation.schemes.ValidationScheme<T> where T : Comparable<T>, T : Number {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.NumberRangeAction(
            { o1, o2 -> o1.compareTo(o2) },
            min = min,
            max = max
        )
    )
}

fun <T> vm.words.ua.utils.validation.schemes.ValidationScheme<T>.isBetweenInclusive(
    min: T,
    max: T
): vm.words.ua.utils.validation.schemes.ValidationScheme<T> where T : Comparable<T>, T : Number {
    return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.add(
        _root_ide_package_.vm.words.ua.utils.validation.actions.NumberRangeAction(
            { o1, o2 -> o1.compareTo(o2) },
            min = min,
            max = max,
            minInclusive = true,
            maxInclusive = true
        )
    )
}
