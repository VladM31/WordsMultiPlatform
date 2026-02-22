package vm.words.ua.core.domain.models.enums

import  vm.words.ua.core.utils.titleCase

enum class Language(
    private val _shortName: String?
) {
    POLISH("pl"),
    ENGLISH("en"),
    GERMAN("de"),

    FRENCH("fr"),
    UKRAINIAN("ua"),
    CZECH("cz"),

    UNDEFINED(null);

    val upperShortName: String = _shortName?.uppercase() ?: "UN"

    val shortName: String
        get() = _shortName.toString()


    val titleCase = this.titleCase()
}