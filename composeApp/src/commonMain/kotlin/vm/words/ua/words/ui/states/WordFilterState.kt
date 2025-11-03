package vm.words.ua.words.ui.states

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.enums.WordSortBy

data class WordFilterState(
    val originalLang: Language? = null,
    val translateLang: Language? = null,
    val original: String? = null,
    val translate: String? = null,
    val categories: List<String>? = null,
    val asc: Boolean = false,
    val sortBy: WordSortBy? = null,
    val isInit: Boolean = false,
    val cefrs: Collection<CEFR>? = null,
)