package vm.words.ua.words.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.subscribes.domain.managers.SubscribeCacheManager
import vm.words.ua.words.domain.managers.SoundManager
import vm.words.ua.words.domain.managers.UserWordManager
import vm.words.ua.words.domain.models.EditUserWord
import vm.words.ua.words.domain.models.enums.WordType
import vm.words.ua.words.ui.actions.WordEditAction
import vm.words.ua.words.ui.states.WordEditState

class WordEditVm(
    private val userWordManager: UserWordManager,
    private val subscribeCacheManager: SubscribeCacheManager,
    private val soundManager: SoundManager,
) : ViewModel() {

    private val mutableState = MutableStateFlow(WordEditState())
    val state: StateFlow<WordEditState> = mutableState

    fun sent(action: WordEditAction) {
        when (action) {
            is WordEditAction.Init -> handleInit(action)
            is WordEditAction.Save -> handleSave()
            is WordEditAction.SetOriginal -> mutableState.update {
                it.copy(original = action.value, originalError = validateOriginal(action.value))
            }

            is WordEditAction.SetLang -> mutableState.update { it.copy(lang = action.value) }
            is WordEditAction.SetTranslate -> mutableState.update {
                it.copy(translate = action.value, translateError = validateTranslate(action.value))
            }

            is WordEditAction.SetTranslateLang -> mutableState.update { it.copy(translateLang = action.value) }
            is WordEditAction.SetCefr -> mutableState.update { it.copy(cefr = action.value) }
            is WordEditAction.SetCategory -> mutableState.update {
                it.copy(category = action.value, categoryError = validateCategory(action.value))
            }

            is WordEditAction.SetDescription -> mutableState.update {
                it.copy(description = action.value, descriptionError = validateDescription(action.value))
            }

            is WordEditAction.SetImage -> mutableState.update { it.copy(image = action.value) }
            is WordEditAction.SetSound -> mutableState.update { it.copy(sound = action.value) }
            is WordEditAction.PlaySound -> {
                handlePlay()
            }
        }
    }

    private fun handlePlay() {
        val soundFile = mutableState.value.sound ?: return
        mutableState.update { it.copy(isPlaying = true) }
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val content = soundFile.readBytes()
                withContext(Dispatchers.Main) {
                    soundManager.playSound(ByteContent(content))
                }

                delay(2500)
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        errorMessage = ErrorMessage(e.message ?: "Error playing sound")
                    )
                }
            } finally {
                mutableState.update { it.copy(isPlaying = false) }
            }
        }
    }

    private fun handleInit(action: WordEditAction.Init) {
        if (mutableState.value.id.isNotEmpty()) return

        val word = action.userWord.word
        val userWord = action.userWord

        viewModelScope.launch(Dispatchers.Default) {
            val isSubscriptionActive = subscribeCacheManager.isActiveSubscribe()
            mutableState.update {
                WordEditState(
                    id = userWord.id,
                    original = word.original,
                    lang = word.lang,
                    translate = word.translate,
                    translateLang = word.translateLang,
                    cefr = word.cefr,
                    category = word.category ?: "",
                    description = word.description ?: "",
                    soundFileName = word.soundLink,
                    imageFileName = word.imageLink,
                    initialOriginal = word.original,
                    initialLang = word.lang,
                    initialTranslate = word.translate,
                    initialTranslateLang = word.translateLang,
                    initialCefr = word.cefr,
                    initialCategory = word.category,
                    initialDescription = word.description,
                    isCustomWord = word.type == WordType.CUSTOM,
                    isSubscriptionActive = isSubscriptionActive,
                )
            }
        }
    }

    private fun handleSave() {
        val currentState = mutableState.value
        if (!currentState.canSave) return

        mutableState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.Default) {
            try {
                userWordManager.update(currentState.toEditUserWord())
                mutableState.update { it.copy(isEnd = true, isLoading = false) }
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = ErrorMessage(e.message ?: "Error updating word")
                    )
                }
            }
        }
    }

    private fun WordEditState.toEditUserWord() = EditUserWord(
        id = id,
        original = original,
        lang = lang,
        translate = translate,
        translateLang = translateLang,
        cefr = cefr,
        category = category.ifBlank { null },
        description = description.ifBlank { null },
        soundFileName = soundFileName?.split("/")?.lastOrNull(),
        imageFileName = imageFileName?.split("/")?.lastOrNull(),
        sound = sound,
        image = image,
    )

    private fun validateOriginal(value: String): String? = when {
        value.isBlank() -> "Original must not be blank"
        value.length > 255 -> "Must be 255 characters or less"
        else -> null
    }

    private fun validateTranslate(value: String): String? = when {
        value.isBlank() -> "Translation must not be blank"
        value.length > 255 -> "Must be 255 characters or less"
        else -> null
    }

    private fun validateCategory(value: String): String? {
        if (value.isBlank()) return null
        return if (value.length > 255) "Must be 255 characters or less" else null
    }

    private fun validateDescription(value: String): String? {
        if (value.isBlank()) return null
        return if (value.length > 1000) "Must be 1000 characters or less" else null
    }
}