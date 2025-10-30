package vm.words.ua.exercise.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.ui.actions.SelectingAnOptionAction
import vm.words.ua.exercise.ui.states.SelectingAnOptionState
import vm.words.ua.words.domain.managers.SoundManager

class SelectingAnOptionVm(
    private val soundManager: SoundManager,
    private val contentManager: ByteContentManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(SelectingAnOptionState())
    val state: StateFlow<SelectingAnOptionState> = mutableState

    fun sent(action: SelectingAnOptionAction) {
        when (action) {
            is SelectingAnOptionAction.Init -> handleInit(action)
            is SelectingAnOptionAction.ChooseWord -> handleChooseWord(action)
            is SelectingAnOptionAction.Next -> handleNext()
            else -> {}
        }
    }

    private fun handleChooseWord(action: SelectingAnOptionAction.ChooseWord) {
        val word = state.value.let { it.words[it.wordIndex] }
        val isCorrect = word.wordId == action.wordId

        val newIndex = state.value.wordIndex + 1
        val isEnd = newIndex == state.value.words.size

        runSound(newIndex, isNeedSound = { it.isSoundAfterAnswer})

        mutableState.value = state.value.copy(
            grades = state.value.grades + if (isCorrect) 1 else 0,
            isEnd = isEnd,
            waitNext = true,
            isCorrect = isCorrect
        )

        viewModelScope.launch(Dispatchers.Default){
//            exerciseStatisticalManager.completeWord(
//                state.value.toWordCompleted()
//            )
        }
    }

    private fun handleNext(){
        val newIndex = state.value.wordIndex + 1
        val isEnd = newIndex == state.value.words.size

        val wordsToChoose = if (isEnd) emptyList() else getChoiceWords(newIndex, state.value.words)
        mutableState.value = state.value.copy(
            wordIndex = if (isEnd) newIndex - 1 else newIndex,
            wordsToChoose = wordsToChoose,
            isEnd = isEnd,
            waitNext = false,
            isCorrect = null
        )
        runSound(mutableState.value.wordIndex, isNeedSound = { it.isSoundBeforeAnswer})
    }

    private fun runSound(newIndex: Int, isNeedSound :  (SelectingAnOptionState) -> Boolean) {
        state.value.words.getOrNull(newIndex)?.soundLink?.let { link ->
            if (!state.value.let { it.isActiveSubscribe && isNeedSound(it) }) {
                return@let
            }
            viewModelScope.launch(Dispatchers.Default) {
                val content = contentManager.downloadByteContent(link)
                withContext(Dispatchers.Main) {
                    soundManager.playSound(content)
                }
            }
        }
    }

    private fun handleInit(action: SelectingAnOptionAction.Init) {
        if (state.value.isInited) return
        val words = action.words.shuffled()

        mutableState.value = state.value.copy(
            isInited = true,
            words = words,
            isActiveSubscribe = action.isActiveSubscribe,
            wordsToChoose = getChoiceWords(state.value.wordIndex,words),
            exercise = action.exerciseType,
            transactionId = action.transactionId,
            isSoundBeforeAnswer = action.isSoundBeforeAnswer,
            isSoundAfterAnswer = action.isSoundAfterAnswer
        )
        runSound(mutableState.value.wordIndex, isNeedSound = { it.isSoundBeforeAnswer})
    }

    private fun getChoiceWords(index: Int, stateWords: List<ExerciseWordDetails>): List<ExerciseWordDetails> {
        val choiceWord = stateWords[index]
        val choiceWords = mutableSetOf<ExerciseWordDetails>()
        val words = stateWords.shuffled()

        choiceWords.add(choiceWord)

        for (word in words) {
            choiceWords.add(word)
            if (choiceWords.size == 3) {
                break
            }
        }

        return choiceWords.toList().shuffled()
    }
}