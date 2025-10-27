package vm.words.ua.subscribes.net.clients.impl

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import vm.words.ua.subscribes.net.clients.PayClient
import vm.words.ua.subscribes.net.requests.CardPayRequest
import vm.words.ua.subscribes.net.requests.GooglePayRequest
import vm.words.ua.subscribes.net.responds.PayRespond
import vm.words.ua.subscribes.net.responds.SubCostRespond
import vm.words.ua.subscribes.net.responds.WaitCardPayRespond
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.managers.UserCacheManager

/**
 * Реализация PayClient на основе Ktor HTTP клиента
 */
class KtorPayClient(
    private val client: HttpClient,
    private val tokenManager: UserCacheManager
) : PayClient {

    override suspend fun payWithCard(request: CardPayRequest): WaitCardPayRespond? {
        return try {
            val token = tokenManager.tokenFlow.value?.value ?: return null
            val response: HttpResponse = client.post("${AppRemoteConfig.baseUrl}/pay/payment/card") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(request)
            }

            if (response.status.isSuccess()) {
                response.body<WaitCardPayRespond>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun payWithGooglePay(request: GooglePayRequest): PayRespond? {
        return try {
            val token = tokenManager.tokenFlow.value?.value ?: return null
            val response: HttpResponse = client.post("${AppRemoteConfig.baseUrl}/pay/payment/google-pay") {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $token")
                setBody(request)
            }

            if (response.status.isSuccess()) {
                response.body<PayRespond>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCosts(platform: String): List<SubCostRespond>? {
        return try {
            val token = tokenManager.tokenFlow.value?.value ?: return null
            val response: HttpResponse = client.get("${AppRemoteConfig.baseUrl}/pay/subscribe/costs") {
                parameter("platform", platform)
                header("Authorization", "Bearer $token")
            }

            if (response.status.isSuccess()) {
                response.body<List<SubCostRespond>>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun waitExpirationDate(dateCacheId: String): PayRespond? {
        return try {
            val token = tokenManager.tokenFlow.value?.value ?: return null
            val response: HttpResponse = client.get("${AppRemoteConfig.baseUrl}/pay/payment/card/date") {
                parameter("dateCacheId", dateCacheId)
                header("Authorization", "Bearer $token")
            }

            if (response.status.isSuccess()) {
                response.body<PayRespond>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

