package vm.words.ua.exercise.domain.mappers

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.data.StartExerciseTransaction
import vm.words.ua.exercise.ui.states.ExerciseSelectState
import vm.words.ua.words.domain.models.UserWord


fun ExerciseSelectState.toStartExerciseTransaction(): StartExerciseTransaction {
    return StartExerciseTransaction(
        transactionId = this.transactionId,
        exercises = this.selectedExercises.keys.map { it.id },
        createdAt = Clock.System.now().toEpochMilliseconds(),
        words = this.words.map { it.toWord() }
    )
}

private fun ExerciseWordDetails.toWord(): StartExerciseTransaction.Word {
    return StartExerciseTransaction.Word(
        wordId = this.wordId,
        userWordId = this.userWordId,
        grade = this.grade,
        dateOfAdded = this.createdAt.toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).toString(),
        lastReadDate = this.lastReadDate.toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).toString(),
        original = this.original,
        translate = this.translate,
        lang = this.lang.toString(),
        translateLang = this.translateLang.toString(),
        cefr = this.cefr.toString(),
        hasDescription = this.description.isNullOrBlank().not(),
        category = this.category,
        hasSound = this.soundLink.isNullOrBlank().not(),
        hasImage = this.imageLink.isNullOrBlank().not()
    )
}

fun UserWord.toExerciseWordDetails(transactionId: String) = ExerciseWordDetails(
    transactionId = transactionId,
    grade = 0,
    userWordId = id,
    wordId = word.id,
    createdAt = createdAt,
    lastReadDate = lastReadDate,
    original = word.original,
    translate = word.translate,
    lang = word.lang,
    translateLang = word.translateLang,
    cefr = word.cefr,
    description = word.description,
    category = word.category,
    soundLink = word.soundLink,
    imageLink = word.imageLink
)