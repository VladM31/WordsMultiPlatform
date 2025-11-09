package vm.words.ua.core.utils


fun Enum<*>.titleCase() : String {
    return name.titleCase()
}

fun String.titleCase() : String {
    if (isEmpty() or isBlank()){
        return this
    }

    return this[0].titlecaseChar() + substring(1).lowercase().replace("_", " ")
}

fun String.toNumbersOnly(): String {
    return this.replace(Regex("[^0-9]"), "")
}