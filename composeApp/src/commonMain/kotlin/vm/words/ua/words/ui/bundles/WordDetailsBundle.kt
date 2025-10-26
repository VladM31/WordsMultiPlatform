package vm.words.ua.words.ui.bundles

import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word

data class WordDetailsBundle(
    val word: Word,
    val userWord: UserWord? = null
)
