package vm.words.ua.words.domain.models

import io.github.vinceglb.filekit.PlatformFile
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

data class SaveWord(
    val word: String,
    val translation: String,
    val language: Language,
    val translationLanguage: Language,
    val cefr: CEFR,

    val category: String? = null,
    val description: String? = null,

    var image: PlatformFile? = null,
    var sound: PlatformFile? = null,
    val needSound: Boolean = false,
)