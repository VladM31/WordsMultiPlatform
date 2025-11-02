package vm.words.ua.words.domain.models.filters

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.domain.models.enums.Language
import vm.words.ua.core.domain.models.filters.Queryable
import vm.words.ua.words.domain.models.enums.UserWordSortBy
import vm.words.ua.words.domain.models.enums.WordType

@Serializable
data class UserWordFilter(
    val userWordIds: Collection<String>? = null,
    val wordIds: Collection<String>? = null,

    val languages: Collection<Language>? = null,
    val translateLanguages: Collection<Language>? = null,
    val categories: Collection<String>? = null,

    val cefrs: Collection<CEFR>? = null,
    val types: Collection<WordType>? = null,


    val translate: String? = null,
    val original: String? = null,


    val sortField: UserWordSortBy = UserWordSortBy.CREATED_AT,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20,
) : Queryable