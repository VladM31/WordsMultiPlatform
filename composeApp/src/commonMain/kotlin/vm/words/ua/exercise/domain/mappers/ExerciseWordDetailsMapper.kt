package vm.words.ua.exercise.domain.mappers

import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.words.domain.models.UserWord

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