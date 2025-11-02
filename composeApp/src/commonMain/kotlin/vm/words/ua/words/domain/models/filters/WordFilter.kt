package vm.words.ua.words.domain.models.filters

import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.domain.models.filters.Queryable
import vm.words.ua.words.domain.models.enums.WordSortBy
import vm.words.ua.words.domain.models.enums.WordType

data class WordFilter(
    val wordIds: Collection<String>? = null,
    val languages: Set<Language>? = null,
    val translateLanguages: Set<Language>? = null,
    val categories: Collection<String>? = null,
    val cefrs: Collection<CEFR>? = null,
    val types: Collection<WordType>? = null,

    val original: String? = null,
    val translate: String? = null,
    val userId: UserId? = null,

    val sortField: WordSortBy = WordSortBy.ORIGIN,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20,
) : Queryable {

    data class UserId(
        val isIn: Boolean = false,
        val id: String? = null
    )
}