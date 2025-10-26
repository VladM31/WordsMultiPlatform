package vm.words.ua.words.domain.models

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

data class Word(
    val id: String,
    val original: String,
    val lang: Language,
    val translate: String,
    val translateLang: Language,
    val cefr: CEFR,
    val description: String?,
    val category: String?,
    val soundLink: String?,
    val imageLink: String?
)