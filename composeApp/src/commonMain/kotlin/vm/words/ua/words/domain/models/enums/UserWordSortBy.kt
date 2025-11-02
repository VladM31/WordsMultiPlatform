package vm.words.ua.words.domain.models.enums

import vm.words.ua.core.utils.titleCase

enum class UserWordSortBy {
    LANG,
    ORIGIN,
    TRANSLATE,
    CATEGORY,
    CREATED_AT,
    LAST_READ_DATE,
    TRANSLATE_LANG;

    val titleCase = this.titleCase()

    companion object {
        private val mapTitleCase = UserWordSortBy.entries.associateBy { it.titleCase }

        fun fromTitleCase(value: String): UserWordSortBy? {
            return mapTitleCase[value]
        }
    }
}