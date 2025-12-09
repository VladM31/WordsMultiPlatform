package vm.words.ua.exercise.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class RecommendationRequest(
    val wordIds: Collection<String>
)
