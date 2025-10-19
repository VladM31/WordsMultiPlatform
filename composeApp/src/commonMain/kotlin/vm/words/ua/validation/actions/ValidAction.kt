package vm.words.ua.validation.actions

import vm.words.ua.validation.models.ValidResult

interface ValidAction<T> {

    fun validate(value: T): ValidResult
}