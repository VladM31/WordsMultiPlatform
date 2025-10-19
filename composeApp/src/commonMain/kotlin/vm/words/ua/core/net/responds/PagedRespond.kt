package vm.words.ua.core.net.responds

import kotlinx.serialization.Serializable

@Serializable
data class PagedRespond<T>(
    val content: List<T>,
    val page: PageInfo
){
    @Serializable
    data class PageInfo(
        val number: Long,
        val size: Long,
        val totalElements: Long,
        val totalPages: Long
    )

    companion object {
        fun <T> empty() = PagedRespond<T>(
            content = emptyList(),
            page = PageInfo(
                number = 0,
                size = 0,
                totalElements = 0,
                totalPages = 0
            )
        )

    }
}
