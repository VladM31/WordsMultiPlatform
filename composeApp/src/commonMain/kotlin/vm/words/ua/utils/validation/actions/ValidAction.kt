package vm.words.ua.utils.validation.actions

interface ValidAction<T> {

    fun validate(value: T): vm.words.ua.utils.validation.models.ValidResult
}