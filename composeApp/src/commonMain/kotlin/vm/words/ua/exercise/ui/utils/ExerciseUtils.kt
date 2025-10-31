package vm.words.ua.exercise.ui.utils

import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise

private val TO_OPTIONS_BY_EXERCISE : Map<Exercise, (ExerciseWordDetails) -> String> = mapOf(
    Exercise.DESCRIPTION_BY_WORDS to { it.original },
    Exercise.WORD_BY_DESCRIPTIONS to { it.description ?: "Description not found, word -> ${it.original}" },
    Exercise.WORD_BY_ORIGINALS to { it.original },
    Exercise.WORD_BY_TRANSLATES to { it.translate },
)

private val TO_TEXT_BY_EXERCISE : Map<Exercise, (ExerciseWordDetails) -> String> = mapOf(
    Exercise.DESCRIPTION_BY_WORDS to { it.description ?: "Description not found, word -> ${it.original}" },
    Exercise.WORD_BY_DESCRIPTIONS to { it.original },
    Exercise.WORD_BY_ORIGINALS to { it.translate },
    Exercise.WORD_BY_TRANSLATES to { it.original },
)

private val IS_SOUND_BEFORE_BY_EXERCISE = mapOf(
    Exercise.DESCRIPTION_BY_WORDS to false,
    Exercise.WORD_BY_DESCRIPTIONS to true,
    Exercise.WORD_BY_ORIGINALS to false,
    Exercise.WORD_BY_TRANSLATES to true,
)

private val IS_SOUND_AFTER_BY_EXERCISE  = mapOf(
    Exercise.DESCRIPTION_BY_WORDS to true,
    Exercise.WORD_BY_DESCRIPTIONS to false,
    Exercise.WORD_BY_ORIGINALS to true,
    Exercise.WORD_BY_TRANSLATES to false,
)

private val ENABLE_IMAGE_BY_EXERCISE  = mapOf(
    Exercise.DESCRIPTION_BY_WORDS to false,
    Exercise.WORD_BY_DESCRIPTIONS to false,
    Exercise.WORD_BY_ORIGINALS to true,
    Exercise.WORD_BY_TRANSLATES to true,
)

fun ExerciseWordDetails.toOptionText(exercise: Exercise): String {
    val mapper = TO_OPTIONS_BY_EXERCISE[exercise]
        ?: throw IllegalArgumentException("No option mapping for exercise: $exercise")
    return mapper(this)
}

fun ExerciseWordDetails.toText(exercise: Exercise): String {
    val mapper = TO_TEXT_BY_EXERCISE[exercise]
        ?: throw IllegalArgumentException("No main text mapping for exercise: $exercise")
    return mapper(this)
}

fun Exercise.isSoundBeforeAnswer(): Boolean {
    return IS_SOUND_BEFORE_BY_EXERCISE[this]
        ?: throw IllegalArgumentException("No sound before answer mapping for exercise: $this")
}

fun Exercise.isSoundAfterAnswer(): Boolean {
    return IS_SOUND_AFTER_BY_EXERCISE[this]
        ?: throw IllegalArgumentException("No sound after answer mapping for exercise: $this")
}

fun Exercise.isEnableImage(): Boolean {
    return ENABLE_IMAGE_BY_EXERCISE[this] ?:
        throw IllegalArgumentException("No enable image mapping for exercise: $this")
}