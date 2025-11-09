package vm.words.ua.words.ui.states

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.words.domain.models.enums.UserWordSortBy

data class UserWordFilterState(
    val isInit: Boolean = false,

    val original: String? = null,
    val translate: String? = null,
    val lang: Language? = null,
    val translateLang: Language? = null,

    val categories: Collection<String>? = null,
    val asc: Boolean = false,
    val sortBy: UserWordSortBy = UserWordSortBy.CREATED_AT,
    val cefrs: Collection<CEFR>? = null,
    override val isEnd: Boolean = false,
) : EndetableState
