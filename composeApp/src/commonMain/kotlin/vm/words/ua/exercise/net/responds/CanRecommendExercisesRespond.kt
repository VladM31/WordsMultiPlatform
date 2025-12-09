package vm.words.ua.exercise.net.responds

import kotlinx.serialization.Serializable

@Serializable
data class CanRecommendExercisesRespond(
    val canRecommend: Boolean
) {
}