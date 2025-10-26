package vm.words.ua.words.ui.actions

import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word

sealed class WordDetailsAction {
    data class Init(val userWord: UserWord?, val word: Word) : WordDetailsAction()
    data object Delete : WordDetailsAction()
    data object PlaySound : WordDetailsAction()
}

