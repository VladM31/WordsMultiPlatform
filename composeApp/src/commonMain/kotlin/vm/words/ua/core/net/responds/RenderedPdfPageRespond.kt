package vm.words.ua.core.net.responds

import vm.words.ua.core.domain.models.ByteContent

data class RenderedPdfPageRespond(
    val content: ByteContent,
    val pageNumber: Int,
    val totalPages: Int,
)