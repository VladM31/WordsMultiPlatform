package vm.words.ua.words.ui.states

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.core.ui.states.ErrorableState

data class WordEditState(
    val id: String = "",
    val original: String = "",
    val lang: Language = Language.ENGLISH,
    val translate: String = "",
    val translateLang: Language = Language.UKRAINIAN,
    val cefr: CEFR = CEFR.A1,
    val category: String = "",
    val description: String = "",
    val soundFileName: String? = null,
    val imageFileName: String? = null,
    val image: PlatformFile? = null,
    val sound: PlatformFile? = null,

    // Initial values for change detection
    val initialOriginal: String = "",
    val initialLang: Language = Language.ENGLISH,
    val initialTranslate: String = "",
    val initialTranslateLang: Language = Language.UKRAINIAN,
    val initialCefr: CEFR = CEFR.A1,
    val initialCategory: String? = null,
    val initialDescription: String? = null,

    // Field-level validation errors
    val originalError: String? = null,
    val translateError: String? = null,
    val categoryError: String? = null,
    val descriptionError: String? = null,

    val isCustomWord: Boolean = false,
    val isSubscriptionActive: Boolean = false,
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
) : EndetableState, ErrorableState {

    val isFormValid: Boolean
        get() = originalError == null && translateError == null &&
                categoryError == null && descriptionError == null &&
                original.isNotBlank() && translate.isNotBlank()

    val hasChanges: Boolean
        get() = original != initialOriginal ||
                lang != initialLang ||
                translate != initialTranslate ||
                translateLang != initialTranslateLang ||
                cefr != initialCefr ||
                (category.ifBlank { null }) != initialCategory ||
                (description.ifBlank { null }) != initialDescription ||
                image != null || sound != null

    val canSave: Boolean
        get() = isFormValid && hasChanges && !isLoading
}