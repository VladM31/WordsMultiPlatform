package vm.words.ua.core.domain.models

import vm.words.ua.core.net.responds.PagedRespond

data class PagedModels<T>(
    val content: List<T>,
    val page: PageInfo
) {
    data class PageInfo(
        val number: Long,
        val size: Long,
        val totalElements: Long,
        val totalPages: Long
    )

    companion object {
        fun <T> empty() = PagedModels<T>(
            content = emptyList(),
            page = PageInfo(
                number = 0,
                size = 0,
                totalElements = 0,
                totalPages = 0
            )
        )

        fun <T, R> of(respond: PagedRespond<T>, transform: (T) -> R) = PagedModels<R>(
            content = respond.content.map(transform),
            page = PageInfo(
                number = respond.page.number,
                size = respond.page.size,
                totalElements = respond.page.totalElements,
                totalPages = respond.page.totalPages
            )
        )
    }
}

