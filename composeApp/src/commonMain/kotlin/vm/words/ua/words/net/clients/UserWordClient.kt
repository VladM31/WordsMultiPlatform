package vm.words.ua.words.net.clients

import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.words.net.requests.DeleteUserWordRequest
import vm.words.ua.words.net.requests.PinUserWordRequest
import vm.words.ua.words.net.requests.UserWordRequest
import vm.words.ua.words.net.responds.UserWordRespond

interface UserWordClient {

    suspend fun findBy(token: String, filter: Map<String, String>): PagedRespond<UserWordRespond>

    suspend fun save(token: String, requests: List<UserWordRequest>)

    suspend fun pin(token: String, requests: List<PinUserWordRequest>): List<UserWordRespond>

    suspend fun delete(token: String, requests: List<DeleteUserWordRequest>)
}