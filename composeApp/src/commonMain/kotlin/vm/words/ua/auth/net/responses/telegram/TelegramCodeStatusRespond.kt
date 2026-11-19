package vm.words.ua.auth.net.responses.telegram

import kotlinx.serialization.Serializable
import vm.words.ua.auth.domain.models.enums.TelegramCodeStatus

@Serializable
data class TelegramCodeStatusRespond(
    val status: TelegramCodeStatus
)
