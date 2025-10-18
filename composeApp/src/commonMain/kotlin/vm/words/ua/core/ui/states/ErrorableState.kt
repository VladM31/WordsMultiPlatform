package vm.words.ua.core.ui.states

import vm.words.ua.core.ui.models.ErrorMessage

interface ErrorableState {
    val errorMessage: ErrorMessage?
}