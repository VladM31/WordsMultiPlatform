package vm.words.ua.auth.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import vm.words.ua.auth.net.clients.UserClient
import vm.words.ua.auth.net.requests.DeleteAccountRequest
import vm.words.ua.auth.net.requests.EditUserRequest
import vm.words.ua.auth.net.requests.SetPasswordRequest
import vm.words.ua.auth.net.requests.google.GoogleSignInRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.utils.throwIfError

class KrotUserClient(
    private val client: HttpClient
) : UserClient {
    private val baseUrl by lazy { "${AppRemoteConfig.baseUrl}/user" }

    override suspend fun editUser(header: Pair<String, String>, req: EditUserRequest): AuthResponse {
        return client.put(baseUrl) {
            contentType(ContentType.Application.Json)
            header(header.first, header.second)
            setBody(req)
        }.throwIfError().body()
    }

    override suspend fun setPassword(header: Pair<String, String>, req: SetPasswordRequest) {
        client.post("$baseUrl/password") {
            contentType(ContentType.Application.Json)
            header(header.first, header.second)
            setBody(req)
        }.throwIfError()
    }

    override suspend fun linkGoogle(
        header: Pair<String, String>,
        req: GoogleSignInRequest
    ): AuthResponse.User {
        return client.post("$baseUrl/identities/google") {
            contentType(ContentType.Application.Json)
            header(header.first, header.second)
            setBody(req)
        }.throwIfError().body()
    }

    override suspend fun deleteAccount(
        header: Pair<String, String>,
        req: DeleteAccountRequest
    ) {
        client.post("$baseUrl/delete") {
            contentType(ContentType.Application.Json)
            header(header.first, header.second)
            setBody(req)
        }.throwIfError()
    }
}
