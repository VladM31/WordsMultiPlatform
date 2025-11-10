package vm.words.ua.core.domain.managers.impl

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsBytes
import vm.words.ua.core.domain.managers.ByteContentManager
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.ByteContent

class ByteContentManagerImpl(
    private val httpClient: HttpClient,
    private val userCacheManager: UserCacheManager
) : ByteContentManager {
    override suspend fun downloadByteContent(url: String, needAuth: Boolean): ByteContent {
        val respond = httpClient.get(url) {
            if (needAuth) {
                header("Authorization", "Bearer ${userCacheManager.token.value}")
            }

        }
        return ByteContent(respond.bodyAsBytes())
    }
}