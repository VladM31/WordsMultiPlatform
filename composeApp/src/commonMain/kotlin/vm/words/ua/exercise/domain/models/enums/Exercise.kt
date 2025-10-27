package vm.words.ua.exercise.domain.models.enums

enum class Exercise(val text: String, val id: Int) {
    WORD_BY_TRANSLATES("Word by translates", id = 2),
    WORD_BY_ORIGINALS("Word by originals", id = 3),
    WORD_BY_DESCRIPTIONS("Word by descriptions", id = 4),
    DESCRIPTION_BY_WORDS("Description by words", id = 5),

    LETTERS_MATCH_BY_TRANSLATION("Letters match by translation", id = 6),
    LETTERS_MATCH_BY_DESCRIPTION("Letters match by description", id = 9),

    COMPARE("Match words", id = 7),
    WORD_BY_WRITE_TRANSLATE("Write by translate", id = 1),
    WORD_BY_WRITE_BY_DESCRIPTION("Write by description", id = 8),

}