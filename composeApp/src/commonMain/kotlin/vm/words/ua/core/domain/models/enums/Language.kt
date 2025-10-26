package vm.words.ua.core.domain.models.enums

import  vm.words.ua.core.utils.titleCase

enum class Language(
    private val _shortName : String?
) {
    POLISH("pl"),
    ENGLISH("en"),
    GERMAN("de"),
//    FRENCH("fr"),
    UKRAINIAN("ua"),
//    RUSSIAN("ru"),
    UNDEFINED(null);

    val upperShortName : String = _shortName?.uppercase() ?: "UN"

    val shortName: String
        get() = _shortName.toString()


    val titleCase = this.titleCase()

    companion object {
        private val mapShortName = entries.associateBy(Language::shortName)
        private val mapTitleCase = entries.associateBy(Language::titleCase)

        fun fromShortName(shortName: String): Language {
            return mapShortName[shortName] ?: UNDEFINED
        }

        fun fromTitleCase(titleCase: String): Language {
            return mapTitleCase[titleCase] ?: UNDEFINED
        }

        fun Language.toBcp47Tag(region: String? = null): String? = when (this) {
            POLISH -> buildTag("pl", region ?: "PL")
            ENGLISH -> buildTag("en", region)
            GERMAN -> buildTag("de", region ?: "DE")
            UKRAINIAN -> buildTag("ua", region ?: "UA")
            UNDEFINED -> null
        }

        private fun buildTag(lang: String, region: String?): String =
            if (region.isNullOrBlank()) lang else "$lang-$region"
    }

    val isDefined: Boolean
        get() = this != UNDEFINED
}