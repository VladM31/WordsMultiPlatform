package vm.words.ua.words.ui.actions

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.Word

sealed interface DefaultAddWordAction {
    data object Add : DefaultAddWordAction

    data class ChangeLanguage(val language: Language) : DefaultAddWordAction
    data class ChangeTranslationLanguage(val language: Language) : DefaultAddWordAction

    data class SetWord(val word: String) : DefaultAddWordAction
    data class SetTranslation(val translation: String) : DefaultAddWordAction
    data class SetDescription(val description: String) : DefaultAddWordAction
    data class SetCategory(val category: String) : DefaultAddWordAction

    data class SetCefr(val cefr: CEFR) : DefaultAddWordAction

    data class SetNeedSound(val needSound: Boolean) : DefaultAddWordAction

    data class SetImage(val image: PlatformFile?) : DefaultAddWordAction
    data class SetSound(val sound: PlatformFile?) : DefaultAddWordAction

    data class Init(val word: Word?) : DefaultAddWordAction
}