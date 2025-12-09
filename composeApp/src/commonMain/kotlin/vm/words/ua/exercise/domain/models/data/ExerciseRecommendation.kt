package vm.words.ua.exercise.domain.models.data

import vm.words.ua.exercise.domain.models.enums.Exercise

data class ExerciseRecommendation(
    val recommendationId: String,
    val exercises: List<Exercise>
)
