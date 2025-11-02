package vm.words.ua.words.net.responds

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.enums.WordType

@Serializable
data class WordRespond(
    val id: String,
    val original: String,
    val translate: String,
    val lang: Language,
    val translateLang: Language,
    val cefr: CEFR,
    val type: WordType,
    val description: String? = null,
    val category: String? = null,
    val soundLink: String? = null,
    val imageLink: String? = null
)
