package vm.words.ua.exercise.domain.managers

import vm.words.ua.exercise.domain.models.data.ExerciseRecommendation
import vm.words.ua.exercise.domain.models.data.RecommendationRequest

interface ExerciseRecommendationManager {

    suspend fun recommendExercises(request: RecommendationRequest): ExerciseRecommendation?

    suspend fun canRecommendExercises(request: RecommendationRequest): Boolean
}
