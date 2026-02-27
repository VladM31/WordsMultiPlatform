package vm.words.ua.words.net.requests

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language

@Serializable
data class UserWordEditRequest(
    val id: String,
    val original: String,
    val lang: Language,
    val translate: String,
    val translateLang: Language,
    val category: String?,
    val soundFileName: String?,
    val imageFileName: String?,
    val description: String?,
    val cefr: CEFR,
)