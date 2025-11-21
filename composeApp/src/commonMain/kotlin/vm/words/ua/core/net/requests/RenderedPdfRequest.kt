package vm.words.ua.core.net.requests

import vm.words.ua.core.domain.models.enums.PdfResource

data class RenderedPdfRequest(
    val resource: PdfResource,
    val pageIndex: Int
)
