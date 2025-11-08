package vm.words.ua.core.net.requests

import kotlinx.serialization.Serializable
import vm.words.ua.core.domain.models.enums.Language

@Serializable
data class AudioGenerationRequest(
    val text: String,
    val language: Language
)
