package vm.words.ua.words.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.subscribes.domain.managers.SubscribeCacheManager
import vm.words.ua.words.domain.managers.UserWordManager
import vm.words.ua.words.domain.models.PinUserWord
import vm.words.ua.words.domain.models.WordByteContent
import vm.words.ua.words.domain.utils.downloadWordByteContent
import vm.words.ua.words.ui.actions.PinUserWordsAction
import vm.words.ua.words.ui.states.PinUserWordsState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PinUserWordsViewModel(
    private val userWordManager: UserWordManager,
    private val subscribeCacheManager: SubscribeCacheManager,
    private val byteContentManager: ByteContentManager
) : ViewModel() {

    private val mutableState: MutableStateFlow<PinUserWordsState> =
        MutableStateFlow(
            PinUserWordsState(
                index = 0
            )
        )

    val state: StateFlow<PinUserWordsState> = mutableState


    private fun setNewValue(factory: (PinUserWordsState) -> PinUserWordsState) {
        mutableState.value = factory(state.value)
    }

    fun sent(action: PinUserWordsAction) {
        when (action) {
            is PinUserWordsAction.Load -> {
                loadWords(action)
            }

            is PinUserWordsAction.Pin -> {
                handlePin()
            }

            is PinUserWordsAction.NextWord -> {
                nextWord()
            }

            is PinUserWordsAction.PreviousWord -> {
                previousWord()
            }

            is PinUserWordsAction.SaveFiles -> {
                saveFiles()
            }

            is PinUserWordsAction.SetImage -> {
                handleSetImage(action)
            }

            is PinUserWordsAction.SetSound -> {
                handleSetSound(action)
            }
        }
    }

    private fun handlePin() {
        if (state.value.words.isEmpty()) return

        viewModelScope.launch(Dispatchers.Default) {
            state.value.words.toPinWords().runCatching {
                userWordManager.pin(this@runCatching)
            }.onFailure {
                println("PinUserWordsViewModel - handlePin: ${it.message}")
            }

            mutableState.value = state.value.copy(
                isEnd = true
            )
        }
    }

    private fun Collection<PinUserWordsState.Word>.toPinWords(): Collection<PinUserWord> {
        return map {
            PinUserWord(
                wordId = it.wordId,
                sound = it.customSound,
                image = it.customImage
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun handleSetImage(action: PinUserWordsAction.SetImage) {
        setNewValue {
            it.copy(
                image = action.image,
                updateId = Uuid.random().toString()

            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun handleSetSound(action: PinUserWordsAction.SetSound) {
        setNewValue {
            it.copy(
                sound = action.sound,
                updateId = Uuid.random().toString()
            )
        }
    }

    private fun loadWords(action: PinUserWordsAction.Load) {
        if (state.value.isInited) return

        viewModelScope.launch(Dispatchers.Default) {
            val isSubscribed = subscribeCacheManager.isActiveSubscribe()

            val contentByWordId = getFileContent(action, isSubscribed)

            val words = action.words.map { word ->
                PinUserWordsState.Word(
                    wordId = word.id,
                    original = word.original,
                    lang = word.lang,
                    customImage = null,
                    customSound = null,
                    originalImage = contentByWordId[word.id]?.imageContent,
                    originalSound = contentByWordId[word.id]?.soundContent
                )
            }

            if (subscribeCacheManager.isActiveSubscribe()) {
                mutableState.value = state.value.copy(words = words, isInited = true)
                return@launch
            }

            words.toPinWords().runCatching {
                userWordManager.pin(this@runCatching)
            }.onFailure {
                println("PinUserWordsViewModel - handlePin: ${it.message}")
            }

            mutableState.value = state.value.copy(
                isEnd = true
            )
        }
    }

    private suspend fun CoroutineScope.getFileContent(
        action: PinUserWordsAction.Load,
        isSubscribed: Boolean
    ): Map<String, WordByteContent> {
        if (isSubscribed.not()) {
            return mapOf()
        }
        return action.words.map {
            async {
                byteContentManager.downloadWordByteContent(word = it)
            }
        }.awaitAll().associateBy { it.wordId }
    }

    private fun nextWord() {
        if (START_INDEX == state.value.index) return
        val newIndex = state.value.index + 1
        if (newIndex >= state.value.words.size) return
        val word = state.value.words[newIndex]
        setNewValue { state ->
            state.copy(
                index = newIndex,
                sound = word.customSound,
                image = word.customImage,
                currentUpdateId = null,
                updateId = null
            )
        }
    }

    private fun previousWord() {
        val newIndex = state.value.index - 1
        if (newIndex < 0) return
        val word = state.value.words[newIndex]

        setNewValue { state ->
            state.copy(
                index = newIndex,
                sound = word.customSound,
                image = word.customImage,
                currentUpdateId = null,
                updateId = null
            )
        }
    }

    private fun saveFiles() {
        val word = state.value.run {
            words[index]
        }

        word.customImage = state.value.image
        word.customSound = state.value.sound
        setNewValue {
            it.copy(
                currentUpdateId = state.value.updateId
            )
        }
    }


    companion object {
        private const val START_INDEX = -1
    }
}