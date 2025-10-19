package vm.words.ua.validation.schemes

import vm.words.ua.validation.actions.NumberRangeAction


fun <T>  ValidationScheme<T>.isGreaterThan(value: T) : ValidationScheme<T>  where T: Comparable<T>,T: Number {
    return add(NumberRangeAction({ o1, o2 -> o1.compareTo(o2) }, min = value))
}

fun <T>  ValidationScheme<T>.isGreaterThanOrEquals(value: T) : ValidationScheme<T>  where T: Comparable<T>,T: Number {
    return add(NumberRangeAction({ o1, o2 -> o1.compareTo(o2) }, min = value, minInclusive = true))
}

fun <T>  ValidationScheme<T>.isLessThan(value: T) : ValidationScheme<T>  where T: Comparable<T>,T: Number {
    return add(NumberRangeAction({ o1, o2 -> o1.compareTo(o2) }, max = value))
}

fun <T>  ValidationScheme<T>.isLessThanOrEquals(value: T) : ValidationScheme<T>  where T: Comparable<T>,T: Number {
    return add(NumberRangeAction({ o1, o2 -> o1.compareTo(o2) }, max = value, maxInclusive = true))
}

fun <T>  ValidationScheme<T>.isBetween(min: T, max: T) : ValidationScheme<T>  where T: Comparable<T>,T: Number {
    return add(NumberRangeAction({ o1, o2 -> o1.compareTo(o2) }, min = min, max = max))
}

fun <T>  ValidationScheme<T>.isBetweenInclusive(min: T, max: T) : ValidationScheme<T>  where T: Comparable<T>,T: Number {
    return add(NumberRangeAction({ o1, o2 -> o1.compareTo(o2) }, min = min, max = max, minInclusive = true, maxInclusive = true))
}
