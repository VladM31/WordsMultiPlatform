package vm.words.ua.subscribes.net.clients

import vm.words.ua.subscribes.net.requests.CardPayRequest
import vm.words.ua.subscribes.net.requests.GooglePayRequest
import vm.words.ua.subscribes.net.responds.PayRespond
import vm.words.ua.subscribes.net.responds.SubCostRespond
import vm.words.ua.subscribes.net.responds.WaitCardPayRespond

interface PayClient {
    suspend fun payWithCard(request: CardPayRequest): WaitCardPayRespond?

    suspend fun payWithGooglePay(request: GooglePayRequest): PayRespond?

    suspend fun getCosts(platform: String): List<SubCostRespond>?

    suspend fun waitExpirationDate(dateCacheId: String): PayRespond?
}

