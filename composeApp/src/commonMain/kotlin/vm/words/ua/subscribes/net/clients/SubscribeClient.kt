package vm.words.ua.subscribes.net.clients

import vm.words.ua.subscribes.net.responds.SubscribeRespond

interface SubscribeClient {
    suspend fun fetch() : SubscribeRespond?
}