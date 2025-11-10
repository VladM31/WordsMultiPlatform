package vm.words.ua.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.settings.ui.states.PdfViewState

open class PdfViewerViewModel(
    private val byteContentManager: ByteContentManager,
    private val linkProvider: () -> String
) : ViewModel() {

    protected val mutableState = MutableStateFlow(PdfViewState())
    val state: StateFlow<PdfViewState> = mutableState

    init {
        viewModelScope.launch(Dispatchers.Default) {
            loadPolicyPdf()
        }
    }

    private suspend fun loadPolicyPdf() {
        val content = byteContentManager.downloadByteContent(linkProvider.invoke())
        mutableState.value = mutableState.value.copy(
            content = content
        )
    }
}