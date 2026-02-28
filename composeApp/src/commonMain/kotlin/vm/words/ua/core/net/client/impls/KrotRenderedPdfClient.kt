package vm.words.ua.core.net.client.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.date.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.net.client.RenderedPdfClient
import vm.words.ua.core.net.requests.RenderedPdfRequest
import vm.words.ua.core.net.responds.RenderedPdfPageRespond

class KrotRenderedPdfClient(
    private val httpClient: HttpClient,
) : RenderedPdfClient {

    private val renderUrl by lazy {
        "${AppRemoteConfig.baseUrl}/utility-api/render/pdf/${RESOURCE_NAME}/page/${PAGE_INDEX_NAME}/"
    }

    // ----- Cache -----

    private data class CacheEntry(
        val response: RenderedPdfPageRespond,
        val timestampMs: Long,
    )

    private val cache = mutableMapOf<String, CacheEntry>()

    private fun nowMs(): Long = getTimeMillis()

    private fun getFromCache(url: String): RenderedPdfPageRespond? {
        val current = nowMs()
        val cached = cache[url] ?: return null

        return if (current - cached.timestampMs <= CACHE_TTL_MS) {
            cached.response
        } else {
            cache.remove(url)
            null
        }
    }

    private fun putToCache(url: String, response: RenderedPdfPageRespond) {
        val current = nowMs()
        cleanupExpired(current)
        cache[url] = CacheEntry(response, current)
    }

    private fun cleanupExpired(current: Long) {
        val iterator = cache.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (current - entry.value.timestampMs > CACHE_TTL_MS) {
                iterator.remove()
            }
        }
    }

    // ----- API -----

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
        getFromCache(url)?.let { return it }

        val respond = httpClient.get(url)
        val bytes = respond.body<ByteArray>()
        val pageNumber = respond.headers[HEADER_PAGE_NUMBER]?.toIntOrNull() ?: 0
        val totalPages = respond.headers[HEADER_TOTAL_PAGES]?.toIntOrNull() ?: 0
        val pageWidth = respond.headers[HEADER_PAGE_WIDTH]?.toIntOrNull() ?: 0
        val pageHeight = respond.headers[HEADER_PAGE_HEIGHT]?.toIntOrNull() ?: 0

        val result = RenderedPdfPageRespond(
            content = ByteContent(bytes),
            pageNumber = pageNumber,
            totalPages = totalPages,
            width = pageWidth,
            height = pageHeight,
        )

        putToCache(url, result)

        return result
    }

    companion object {
        private const val RESOURCE_NAME = "resource"
        private const val PAGE_INDEX_NAME = "pageIndex"
        private const val HEADER_PAGE_NUMBER = "X-Page-Number"
        private const val HEADER_TOTAL_PAGES = "X-Total-Pages"
        private const val HEADER_PAGE_WIDTH = "X-Page-Width"
        private const val HEADER_PAGE_HEIGHT = "X-Page-Height"

        private const val CACHE_TTL_MS: Long = 5 * 60 * 1000L
    }
}
