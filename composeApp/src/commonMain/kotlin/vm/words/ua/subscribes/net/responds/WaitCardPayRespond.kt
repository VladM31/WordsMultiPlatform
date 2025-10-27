package vm.words.ua.subscribes.net.responds

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WaitCardPayRespond(
    @SerialName("dateCacheId")
    val dateCacheId: String,
    @SerialName("message")
    val message: String? = null
)

