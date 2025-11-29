package vm.words.ua.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.settings.ui.states.PdfViewState

open class PdfViewerViewModel(
    private val byteContentManager: ByteContentManager,
    private val linkProvider: () -> String,
    private val defaultLinkProvider: () -> String,
) : ViewModel() {

    protected val mutableState = MutableStateFlow(
        PdfViewState(
            link = linkProvider(),
        )
    )
    val state: StateFlow<PdfViewState> = mutableState

    init {
        viewModelScope.launch(Dispatchers.Default) {
            loadPdf()
        }
    }

    private suspend fun loadPdf() {
        if (currentPlatform() == AppPlatform.WASM) {
            mutableState.value = mutableState.value.copy(
                content = ByteContent(linkProvider.invoke().toByteArray(Charsets.UTF_8)),
                link = defaultLinkProvider()
            )
            return
        }
        try {
            val content = byteContentManager.downloadByteContent(
                url = linkProvider.invoke(),
                needAuth = false
            )
            mutableState.value = mutableState.value.copy(
                content = content
            )
        } catch (e: Exception) {
            e.printStackTrace()
            mutableState.value = mutableState.value.copy(
                errorMessage = ErrorMessage(
                    e.message ?: "Unknown error occurred while loading PDF."
                )
            )
        }
    }
}