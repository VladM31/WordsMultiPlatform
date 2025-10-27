package vm.words.ua.subscribes.net.responds

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribeRespond(
    @SerialName("_expirationDate")
    val expirationDate: Instant
)