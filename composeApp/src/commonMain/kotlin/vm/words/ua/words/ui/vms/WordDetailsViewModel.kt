package vm.words.ua.words.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.words.ui.actions.WordDetailsAction
import vm.words.ua.words.ui.states.WordDetailsState

class WordDetailsViewModel(
    private val byteContentManager : ByteContentManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(WordDetailsState())
    val state: StateFlow<WordDetailsState> = mutableState

    fun sent(action: WordDetailsAction) {
        when (action) {
            is WordDetailsAction.Init -> fetchUserWord(action)
            is WordDetailsAction.Delete -> handleDelete()
            is WordDetailsAction.PlaySound -> playSound()
        }
    }

    private fun fetchUserWord(action: WordDetailsAction.Init) {
        val isLoading = action.word.imageLink.isNullOrBlank().not()
                && action.word.soundLink.isNullOrBlank().not()

        mutableState.value = mutableState.value.copy(
            isLoading = isLoading,
            userWord = action.userWord,
            word = action.word
        )

        if(isLoading.not()){
            return
        }

        viewModelScope.launch(Dispatchers.Default) {

            try {
                val imageTask = async {
                    getContent(action.word.imageLink)
                }
                val soundTask = async {
                    getContent(action.word.soundLink)
                }

                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    image = imageTask.await(),
                    sound = soundTask.await()
                )
            }catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    errorMessage = ErrorMessage(e.message ?: "Error loading image")
                )
            }
        }
    }

    private suspend fun getContent(link : String?) : ByteContent?{
        if (link.isNullOrBlank()){
            return null
        }
        return byteContentManager.downloadByteContent(link)
    }



    private fun handleDelete() {
        viewModelScope.launch {
            try {
                // TODO: Delete UserWord through manager
                mutableState.value = mutableState.value.copy(isDeleted = true)
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.value = mutableState.value.copy(
                    errorMessage = ErrorMessage(e.message ?: "Error deleting word")
                )
            }
        }
    }

    private fun playSound() {
        mutableState.value = mutableState.value.copy(isPlayingSound = true)
        viewModelScope.launch {
            try {
                val soundUrl = state.value.userWord?.word?.soundLink ?: return@launch

                // TODO: Play sound using MediaPlayer or other audio solution
                // Sound URL will be cached and played

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mutableState.value = mutableState.value.copy(isPlayingSound = false)
            }
        }
    }
}