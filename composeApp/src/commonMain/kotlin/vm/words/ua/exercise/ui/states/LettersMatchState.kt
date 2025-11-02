package vm.words.ua.exercise.ui.states

import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class LettersMatchState(
    val endLetter: String = "⏎",
    val letterIndex: Int = 0,
    val originalWord: String = "",
    val resultWord: String = endLetter,
    val letters: List<Letter> = emptyList(),
    val isNext: Boolean = false,
    val grade: Int = 3,
    val attempts: Int = 0,
    val errorLetter: ErrorLetter? = null,
    val grades: List<Int> = emptyList(),

    override val transactionId: String = "",
    override val exercise: Exercise = Exercise.LETTERS_MATCH_BY_TRANSLATION,

    val wordIndex: Int = 0,
    override val words: List<ExerciseWordDetails> = emptyList(),
    val isActiveSubscribe: Boolean = false,
    val isInited: Boolean = false,
    override val isEnd: Boolean = false,
) : ExerciseState{

    fun currentLetterChar() = originalWord[letterIndex]

    fun currentWord() = words[wordIndex]

    data class Letter(
        val letter: Char,
        val id: String,
    ){
        companion object{
            @OptIn(ExperimentalUuidApi::class)
            fun from(letter: Char) = Letter(letter, Uuid.random().toString())
        }
    }

    data class ErrorLetter(
        val letter: Letter
    ){

        companion object{
            fun from(letter: Char, id: String) = ErrorLetter(Letter(letter,id))
        }
    }
}