package vm.words.ua.exercise.net.clients

import vm.words.ua.exercise.net.requests.RecommendationRequest
import vm.words.ua.exercise.net.responds.CanRecommendExercisesRespond
import vm.words.ua.exercise.net.responds.ExerciseRecommendationRespond

interface ExerciseRecommendationClient {

    suspend fun recommendExercises(
        request: RecommendationRequest,
        vararg additionalHeaders: Pair<String, String> = emptyArray()
    ): ExerciseRecommendationRespond

    suspend fun canRecommendExercises(
        request: RecommendationRequest,
        vararg additionalHeaders: Pair<String, String> = emptyArray()
    ): CanRecommendExercisesRespond;
}