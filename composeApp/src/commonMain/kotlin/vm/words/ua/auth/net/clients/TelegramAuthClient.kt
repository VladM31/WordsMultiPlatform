package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.TelegramAuthLoginReq
import vm.words.ua.auth.net.requests.TelegramAuthStartLoginReq
import vm.words.ua.auth.net.responses.TelegramAuthRespond
import vm.words.ua.auth.net.responses.TelegramLoginRespond

interface TelegramAuthClient {
    suspend fun startLogin(request: TelegramAuthStartLoginReq): TelegramAuthRespond

    suspend fun login(request: TelegramAuthLoginReq): TelegramLoginRespond

}