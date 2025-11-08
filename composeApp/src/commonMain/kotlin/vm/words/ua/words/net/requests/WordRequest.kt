package vm.words.ua.words.net.requests

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

data class WordRequest(
    val original: String,
    val lang: Language,

    val translate: String,
    val translateLang: Language,

    val category: String?,

    val description: String?,
    val cefr: CEFR,
)