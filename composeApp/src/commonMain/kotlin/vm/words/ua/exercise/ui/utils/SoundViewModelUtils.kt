package vm.words.ua.exercise.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.words.domain.managers.SoundManager

fun ViewModel.runSound(
    soundLink: String?,
    soundManager: SoundManager,
    contentManager: ByteContentManager
) {
    if (soundLink.isNullOrBlank()){
        return
    }
    viewModelScope.launch(Dispatchers.Default) {
        val content = contentManager.downloadByteContent(soundLink)
        withContext(Dispatchers.Main) {
            soundManager.playSound(content)
        }
    }
}