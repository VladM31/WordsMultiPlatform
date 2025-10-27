package vm.words.ua.subscribes.net.responds

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.Currency
import vm.words.ua.subscribes.domain.models.enums.Platforms

@Serializable
data class SubCostRespond(
    @SerialName("id")
    val id: String,
    @SerialName("platform")
    val platform: Platforms,
    @SerialName("cost")
    val cost: Double,
    @SerialName("currency")
    val currency: Currency,
    @SerialName("description")
    val description: String? = null
)

