package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.telegram.TelegramAuthLoginReq
import vm.words.ua.auth.net.requests.telegram.TelegramAuthStartLoginReq
import vm.words.ua.auth.net.requests.telegram.TelegramMiniAppLoginRequest
import vm.words.ua.auth.net.responses.TelegramLoginRespond
import vm.words.ua.auth.net.responses.telegram.TelegramAuthRespond
import vm.words.ua.auth.net.responses.telegram.TelegramMiniAppRespond

interface TelegramAuthClient {
    suspend fun startLogin(request: TelegramAuthStartLoginReq): TelegramAuthRespond

    suspend fun login(request: TelegramAuthLoginReq): TelegramLoginRespond

    suspend fun login(req: TelegramMiniAppLoginRequest): TelegramMiniAppRespond
}