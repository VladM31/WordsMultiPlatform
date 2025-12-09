package vm.words.ua.exercise.net.responds

import kotlinx.serialization.Serializable
import vm.words.ua.exercise.domain.models.enums.Exercise

@Serializable
data class ExerciseRecommendationRespond(
    val recommendationId: String,
    val exercises: List<Exercise>
)
