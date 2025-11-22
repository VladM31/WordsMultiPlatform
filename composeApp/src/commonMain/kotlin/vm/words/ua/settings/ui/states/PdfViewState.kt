package vm.words.ua.settings.ui.states

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState

data class PdfViewState(
    val content: ByteContent? = null,
    val link: String = "",
    override val errorMessage: ErrorMessage? = null
) : ErrorableState
