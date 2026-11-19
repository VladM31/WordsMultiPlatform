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
import vm.words.ua.auth.net.responses.telegram.TelegramCodeStatusRespond
import vm.words.ua.auth.net.responses.telegram.TelegramMiniAppRespond
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.exceptions.ApiException
import vm.words.ua.core.net.utils.throwIfError

class KrotTelegramAuthClient(
    private val httpClient: HttpClient
) : TelegramAuthClient {

    private val baseUrl by lazy {
        "${AppRemoteConfig.baseUrl}/auth"
    }

    /**
     * `POST /auth/telegram-start-login`: единый метод входа и регистрации.
     *
     * 200: код входа. 400 с текстом "not known to the Telegram bot": бот не знает номер,
     * пользователь должен открыть бота и отправить контакт. 404: аккаунт заблокирован.
     * 409, 429 (с `Retry-After`), 503 и остальные 400 приходят как [ApiException].
     */
    override suspend fun startLogin(request: TelegramAuthStartLoginReq): TelegramAuthRespond {
        val respond = httpClient.post("${baseUrl}/telegram-start-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (respond.status.isSuccess()) {
            return TelegramAuthRespond(
                isNotFound = false,
                code = respond.bodyAsText()
            )
        }

        val body = runCatching { respond.bodyAsText() }.getOrDefault("")
        if (respond.status == HttpStatusCode.BadRequest && body.isUnknownToBot()) {
            return TelegramAuthRespond(
                isNotFound = true,
                code = null
            )
        }

        throw ApiException(
            status = respond.status.value,
            body = body,
            retryAfterSeconds = respond.headers[HttpHeaders.RetryAfter]?.toLongOrNull()
        )
    }

    override suspend fun login(request: TelegramAuthLoginReq): TelegramLoginRespond {
        return httpClient.post("${baseUrl}/telegram-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.throwIfError().body()
    }

    override suspend fun login(req: TelegramMiniAppLoginRequest): TelegramMiniAppRespond {
        return httpClient.post("${baseUrl}/telegram/mini-app/login") {
            contentType(ContentType.Application.Json)
            setBody(req)
        }.throwIfError().body()
    }

    override suspend fun codeStatus(code: String): TelegramCodeStatusRespond {
        return httpClient.get("${baseUrl}/telegram-status") {
            parameter("code", code)
        }.throwIfError().body()
    }

    private fun String.isUnknownToBot(): Boolean {
        return contains("not known to the Telegram bot", ignoreCase = true)
    }
}
