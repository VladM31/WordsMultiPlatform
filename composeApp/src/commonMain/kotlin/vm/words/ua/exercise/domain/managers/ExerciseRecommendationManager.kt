package vm.words.ua.exercise.domain.managers

import vm.words.ua.exercise.domain.models.data.ExerciseRecommendation
import vm.words.ua.exercise.domain.models.data.RecommendationOptions

interface ExerciseRecommendationManager {

    suspend fun recommendExercises(request: RecommendationOptions): ExerciseRecommendation?

    suspend fun canRecommendExercises(request: RecommendationOptions): Boolean
}
