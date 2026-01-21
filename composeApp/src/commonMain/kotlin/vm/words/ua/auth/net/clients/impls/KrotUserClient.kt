package vm.words.ua.auth.net.clients.impls

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import vm.words.ua.auth.net.clients.UserClient
import vm.words.ua.auth.net.requests.DeleteAccountRequest
import vm.words.ua.core.config.AppRemoteConfig

class KrotUserClient(
    private val client: HttpClient
) : UserClient {
    private val deleteUrl = "${AppRemoteConfig.baseUrl}/user/delete"

    override suspend fun deleteAccount(
        header: Pair<String, String>,
        req: DeleteAccountRequest
    ): Boolean {
        return try {
            val response: HttpResponse = client.post(deleteUrl) {
                contentType(ContentType.Application.Json)
                header(header.first, header.second)
                setBody(req)
            }

            response.status.isSuccess()
        } catch (e: Exception) {
            false
        }
    }
}