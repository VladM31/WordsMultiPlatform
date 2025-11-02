package vm.words.ua.exercise.net.clients

import vm.words.ua.exercise.net.responds.EndExerciseTransactionRequest
import vm.words.ua.exercise.net.responds.StartExerciseTransactionRequest
import vm.words.ua.exercise.net.responds.WordCompletedRequest

interface ExerciseStatisticalClient {

    suspend fun startExercise(
        request: StartExerciseTransactionRequest,
        vararg additionalHeaders: Pair<String, String> = emptyArray()
    );

    suspend fun completeWord(
        request: WordCompletedRequest,
        vararg additionalHeaders: Pair<String, String> = emptyArray()
    );

    suspend fun endExercise(
        request: EndExerciseTransactionRequest,
        vararg additionalHeaders: Pair<String, String> = emptyArray()
    );
}