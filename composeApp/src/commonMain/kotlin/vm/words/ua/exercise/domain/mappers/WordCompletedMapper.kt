package vm.words.ua.exercise.domain.mappers

import kotlinx.datetime.Clock
import vm.words.ua.exercise.domain.models.data.WordCompleted
import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.ui.states.LettersMatchState
import vm.words.ua.exercise.ui.states.MatchWordsState
import vm.words.ua.exercise.ui.states.SelectingAnOptionState


fun MatchWordsState.toWordCompleted()  : WordCompleted {
    val currentWord = originalWords[original?.index ?: 0].word

    return WordCompleted(
        transactionId = transactionId,
        wordId = currentWord.wordId,
        userWordId = currentWord.userWordId,
        exerciseId = Exercise.COMPARE.id,
        attempts = this.attempts,
        isCorrect = attempts < 3,
        completedAt = Clock.System.now().toEpochMilliseconds()
    )
}

fun SelectingAnOptionState.toWordCompleted()  : WordCompleted{
    return WordCompleted(
        transactionId = transactionId,
        wordId = currentWord().wordId,
        userWordId = currentWord().userWordId,
        exerciseId =  exercise.id,
        attempts = 0,
        isCorrect = isCorrect == true,
        completedAt = Clock.System.now().toEpochMilliseconds()
    )
}

fun LettersMatchState.toWordCompleted() : WordCompleted {
    val currentWord = this.currentWord()


    return WordCompleted(
        transactionId = transactionId,
        wordId = currentWord.wordId,
        userWordId = currentWord.userWordId,
        exerciseId = this.exerciseType.id,
        attempts = this.attempts,
        isCorrect = attempts < 3,
        completedAt = Clock.System.now().toEpochMilliseconds()
    )
}