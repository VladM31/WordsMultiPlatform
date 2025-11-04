package vm.words.ua.words.ui.actions

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.words.domain.models.enums.WordSortBy
import vm.words.ua.words.domain.models.filters.WordFilter

sealed interface WordFilterAction {
    data class SetOriginalLang(val value: Language?) : WordFilterAction
    data class SetTranslateLang(val value: Language?) : WordFilterAction
    data class SetOriginal(val value: String?) : WordFilterAction
    data class SetTranslate(val value: String?) : WordFilterAction
    data class SetCategories(val value: List<String>?) : WordFilterAction
    data class SetAsc(val value: Boolean) : WordFilterAction
    data class SetSortBy(val value: WordSortBy?) : WordFilterAction
    data class Init(val filterId: Long, val value: WordFilter) : WordFilterAction
    data class SetCefr(val value: CEFR?) : WordFilterAction
    data class SetCefrs(val value: Collection<CEFR>?) : WordFilterAction

    data object Clear : WordFilterAction
}