package vm.words.ua.exercise.domain.managers

import vm.words.ua.exercise.domain.models.data.EndExerciseTransaction
import vm.words.ua.exercise.domain.models.data.StartExerciseTransaction
import vm.words.ua.exercise.domain.models.data.WordCompleted

interface ExerciseStatisticalManager {

    suspend fun startExercise(data: StartExerciseTransaction);

    suspend fun completeWord(data: WordCompleted);

    suspend fun endExercise(data: EndExerciseTransaction);
}