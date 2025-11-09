package vm.words.ua.words.ui.actions

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.enums.UserWordSortBy
import vm.words.ua.words.domain.models.filters.UserWordFilter

sealed interface UserWordFilterAction {
    data class Init(val filter: UserWordFilter) : UserWordFilterAction

    data class SetOriginalLang(val value: Language?) : UserWordFilterAction
    data class SetTranslateLang(val value: Language?) : UserWordFilterAction
    data class SetOriginal(val value: String?) : UserWordFilterAction
    data class SetTranslate(val value: String?) : UserWordFilterAction

    data class SetCategories(val value: List<String>?) : UserWordFilterAction
    data class SetSortBy(val sortBy: UserWordSortBy) : UserWordFilterAction
    data class SetAsc(val asc: Boolean) : UserWordFilterAction
    data class SetCefr(val cefr: CEFR?) : UserWordFilterAction

    data object Clear : UserWordFilterAction

    data object Find : UserWordFilterAction
}