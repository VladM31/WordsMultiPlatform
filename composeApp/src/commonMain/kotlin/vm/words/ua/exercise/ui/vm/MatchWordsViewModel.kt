package vm.words.ua.exercise.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.exercise.domain.managers.ExerciseStatisticalManager
import vm.words.ua.exercise.domain.mappers.toWordCompleted
import vm.words.ua.exercise.domain.models.data.MatchWordsBox
import vm.words.ua.exercise.ui.actions.MatchWordsAction
import vm.words.ua.exercise.ui.states.MatchWordsState

class MatchWordsViewModel(
    private val exerciseStatisticalManager: ExerciseStatisticalManager
) : ViewModel() {
    private var positionKeeper = 0
    private val mutableState = MutableStateFlow(MatchWordsState())
    val state: StateFlow<MatchWordsState> = mutableState

    fun sent(action: MatchWordsAction) {
        when (action) {
            is MatchWordsAction.Init -> handleInit(action)
            is MatchWordsAction.Click -> handleClick(action)

        }
    }

    private fun handleClick(action: MatchWordsAction.Click){
        val wordBox = MatchWordsState.WordBox(action.word.userWordId, action.index, action.word.original, action.word.translate)
        val newState = if (action.isOriginal){
            mutableState.value.copy(original = wordBox)
        } else {
            mutableState.value.copy(translate = wordBox)
        }

        // Ждем выбора обоих слов
        if (newState.original == null || newState.translate == null) {
            mutableState.value = newState
            return
        }

        val isMistake = newState.original.id != newState.translate.id
                && newState.original.original.trim() != newState.translate.original.trim()
                && newState.original.translate.trim() != newState.translate.translate.trim()
        val originalWords = newState.originalWords.toMutableList()
        val translateWords = newState.translateWords.toMutableList()

        if (isMistake.not()){
            val position = positionKeeper++
            originalWords[newState.original.index] = originalWords[newState.original.index].copy(position = position)
            translateWords[newState.translate.index] = translateWords[newState.translate.index].copy(position = position)

            val isEnd = position == newState.words.size - 1

            viewModelScope.launch(Dispatchers.Default) {
                exerciseStatisticalManager.completeWord(
                    newState.toWordCompleted()
                )
            }

            mutableState.value = newState.copy(
                original = null,
                translate = null,
                originalWords = originalWords.sorted(),
                translateWords = translateWords.sorted(),
                isEnd = isEnd,
                attempts = 0
            )
            return
        }

        viewModelScope.launch(Dispatchers.Main.immediate) {
            originalWords[newState.original.index] = originalWords[newState.original.index].copy(isMistake = true)
            translateWords[newState.translate.index] = translateWords[newState.translate.index].copy(isMistake = true)
            mutableState.value = newState.copy(
                originalWords = originalWords.toList(),
                translateWords = translateWords.toList()
            )
            delay(1000)
            originalWords[newState.original.index] = originalWords[newState.original.index].copy(isMistake = false)
            translateWords[newState.translate.index] = translateWords[newState.translate.index].copy(isMistake = false)
            mutableState.value = newState.copy(
                original = null,
                translate = null,
                attempts = newState.attempts + 1,
                originalWords = originalWords,
                translateWords = translateWords
            )
        }
    }

    private fun handleInit(action: MatchWordsAction.Init) {
        if (state.value.isInited && state.value.transactionId == action.transactionId){
            return
        }
        val words = action.words.shuffled()
        positionKeeper = 0
        mutableState.value = MatchWordsState().copy(
            words = words,
            originalWords = words.map { MatchWordsBox(it) }.shuffled(),
            translateWords = words.map { MatchWordsBox(it) }.shuffled(),
            isInited = true,
            transactionId = action.transactionId,
        )
    }

}