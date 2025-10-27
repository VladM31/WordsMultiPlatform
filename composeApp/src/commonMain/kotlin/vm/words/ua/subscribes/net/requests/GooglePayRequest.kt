package vm.words.ua.subscribes.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class GooglePayRequest(
    val token: String,
    val amount: Double
)

