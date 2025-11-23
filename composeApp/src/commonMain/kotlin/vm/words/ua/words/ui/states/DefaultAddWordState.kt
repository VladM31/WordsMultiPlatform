package vm.words.ua.words.ui.states

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.core.ui.states.ErrorableState

data class DefaultAddWordState(
    val originalWord: String = "",
    val language: Language = Language.ENGLISH,
    val translationLanguage: Language = Language.ENGLISH,
    val translation: String = "",
    val category: String = "",
    val description: String = "",
    val cefr: CEFR = CEFR.A1,
    var image: PlatformFile? = null,
    var sound: PlatformFile? = null,
    val needSound: Boolean = false,
    val isSubscribe: Boolean? = null,
    var isLoading: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false,
    val isInited: Boolean = false
) : EndetableState, ErrorableState