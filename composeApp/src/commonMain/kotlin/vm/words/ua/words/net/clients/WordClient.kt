package vm.words.ua.words.net.clients

import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.words.net.responds.WordRespond

interface WordClient {

    suspend fun findBy(
        token: String,
        filter: Map<String, String>
    ): PagedRespond<WordRespond>
}