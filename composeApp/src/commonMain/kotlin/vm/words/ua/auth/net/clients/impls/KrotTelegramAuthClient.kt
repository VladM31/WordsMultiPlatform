package vm.words.ua.auth.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import vm.words.ua.auth.net.clients.TelegramAuthClient
import vm.words.ua.auth.net.requests.telegram.TelegramAuthLoginReq
import vm.words.ua.auth.net.requests.telegram.TelegramAuthStartLoginReq
import vm.words.ua.auth.net.requests.telegram.TelegramMiniAppLoginRequest
import vm.words.ua.auth.net.responses.TelegramLoginRespond
import vm.words.ua.auth.net.responses.telegram.TelegramAuthRespond
import vm.words.ua.auth.net.responses.telegram.TelegramMiniAppRespond
import vm.words.ua.core.config.AppRemoteConfig

class KrotTelegramAuthClient(
    private val httpClient: HttpClient
) : TelegramAuthClient {

    private val baseUrl by lazy {
        "${AppRemoteConfig.baseUrl}/auth"
    }

    override suspend fun startLogin(request: TelegramAuthStartLoginReq): TelegramAuthRespond {
        val respond = httpClient.post("${baseUrl}/telegram-start-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (respond.status.isSuccess()){
            return TelegramAuthRespond(
                isNotFound = false,
                code = respond.bodyAsText()
            )
        }
        if (respond.status == io.ktor.http.HttpStatusCode.NotFound){
            return TelegramAuthRespond(
                isNotFound = true,
                code = null
            )
        }
        throw Exception(respond.bodyAsText())
    }

    override suspend fun login(request: TelegramAuthLoginReq): TelegramLoginRespond {
        val respond = httpClient.post("${baseUrl}/telegram-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return respond.body()
    }

    override suspend fun login(req: TelegramMiniAppLoginRequest): TelegramMiniAppRespond {
        val respond = httpClient.post("${baseUrl}/telegram/mini-app/login") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }
        return respond.body()
    }
}