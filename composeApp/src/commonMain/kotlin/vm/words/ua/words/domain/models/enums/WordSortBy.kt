package vm.words.ua.words.domain.models.enums

import vm.words.ua.core.utils.titleCase

enum class WordSortBy {
    ORIGIN,
    TRANSLATE,
    LANG,
    TRANSLATE_LANG,
    CATEGORY;

    val titleCase = this.titleCase()

    companion object {
        private val mapTitleCase = entries.associateBy { it.titleCase }

        fun fromTitleCase(value: String): WordSortBy? {
            return mapTitleCase[value]
        }
    }
}