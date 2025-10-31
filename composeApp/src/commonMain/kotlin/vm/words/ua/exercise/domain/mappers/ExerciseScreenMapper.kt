package vm.words.ua.exercise.domain.mappers

import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.navigation.Screen

private val SCREEN_BY_EXERCISE = mapOf(
    Exercise.DESCRIPTION_BY_WORDS to Screen.OptionDescriptionByWords,
    Exercise.WORD_BY_DESCRIPTIONS to Screen.OptionWordByDescription,
    Exercise.WORD_BY_ORIGINALS to Screen.OptionWordByOriginal,
    Exercise.WORD_BY_TRANSLATES to Screen.OptionWordByTranslate,
    Exercise.LETTERS_MATCH_BY_DESCRIPTION to Screen.LetterMatchByDescription,
    Exercise.LETTERS_MATCH_BY_TRANSLATION to Screen.LetterMatchByTranslation,
    Exercise.COMPARE to Screen.MatchWords,
    Exercise.WORD_BY_WRITE_BY_DESCRIPTION to Screen.WriteByImageAndDescription,
    Exercise.WORD_BY_WRITE_TRANSLATE to Screen.WriteByImageAndTranslation
)

fun Exercise.toScreen(): Screen {
    return SCREEN_BY_EXERCISE[this]
        ?: throw IllegalArgumentException("No screen mapping for exercise: $this")
}