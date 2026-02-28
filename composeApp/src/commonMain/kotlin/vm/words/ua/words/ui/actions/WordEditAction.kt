package vm.words.ua.words.ui.actions

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.UserWord

sealed interface WordEditAction {
    data class Init(val userWord: UserWord) : WordEditAction
    data object Save : WordEditAction
    data object PlaySound : WordEditAction

    data class SetOriginal(val value: String) : WordEditAction
    data class SetLang(val value: Language) : WordEditAction
    data class SetTranslate(val value: String) : WordEditAction
    data class SetTranslateLang(val value: Language) : WordEditAction
    data class SetCefr(val value: CEFR) : WordEditAction
    data class SetCategory(val value: String) : WordEditAction
    data class SetDescription(val value: String) : WordEditAction

    data class SetImage(val value: PlatformFile?) : WordEditAction
    data class SetSound(val value: PlatformFile?) : WordEditAction
}