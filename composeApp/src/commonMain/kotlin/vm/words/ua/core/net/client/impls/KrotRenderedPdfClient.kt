package vm.words.ua.core.net.client.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.net.client.RenderedPdfClient
import vm.words.ua.core.net.requests.RenderedPdfRequest
import vm.words.ua.core.net.responds.RenderedPdfPageRespond

class KrotRenderedPdfClient(
    private val httpClient: HttpClient,
) : RenderedPdfClient {
    private val renderUrl by lazy { "${AppRemoteConfig.baseUrl}/utility-api/render/pdf/${RESOURCE_NAME}/page/${PAGE_INDEX_NAME}/" }

    override suspend fun renderPage(request: RenderedPdfRequest): RenderedPdfPageRespond {
        val url = renderUrl
            .replace(RESOURCE_NAME, request.resource.name)
            .replace(PAGE_INDEX_NAME, request.pageIndex.toString())
        return getRespond(url)
    }

    override suspend fun renderPage(
        link: String,
        pageIndex: Int
    ): RenderedPdfPageRespond {

        val url = "$link$pageIndex/"
        return getRespond(url)
    }

    private suspend fun getRespond(url: String): RenderedPdfPageRespond {
        val respond = httpClient.get(url)
        val bytes = respond.body<ByteArray>()
        val pageNumber = respond.headers[HEADER_PAGE_NUMBER]?.toIntOrNull() ?: 0
        val totalPages = respond.headers[HEADER_TOTAL_PAGES]?.toIntOrNull() ?: 0
        return RenderedPdfPageRespond(
            content = ByteContent(bytes),
            pageNumber = pageNumber,
            totalPages = totalPages
        )
    }

    companion object {
        private const val RESOURCE_NAME = "resource"
        private const val PAGE_INDEX_NAME = "pageIndex"
        private const val HEADER_PAGE_NUMBER = "X-Page-Number"
        private const val HEADER_TOTAL_PAGES = "X-Total-Pages"
    }
}
