package vm.words.ua.subscribes.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class CardPayRequest(
    val cardNumber: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvc: String,
    val amount: Double
)

