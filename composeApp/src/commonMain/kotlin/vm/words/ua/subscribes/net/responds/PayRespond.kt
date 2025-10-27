package vm.words.ua.subscribes.net.responds

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PayRespond(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String? = null
)

