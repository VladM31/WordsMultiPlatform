package vm.words.ua.core.net.client

import vm.words.ua.core.net.requests.RenderedPdfRequest
import vm.words.ua.core.net.responds.RenderedPdfPageRespond

interface RenderedPdfClient {

    suspend fun renderPage(request: RenderedPdfRequest): RenderedPdfPageRespond

    suspend fun renderPage(link: String, pageIndex: Int): RenderedPdfPageRespond
}