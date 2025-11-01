package vm.words.ua.exercise.domain.managers.impls

import vm.words.ua.exercise.domain.managers.ExerciseStatisticalManager
import vm.words.ua.exercise.domain.models.data.EndExerciseTransaction
import vm.words.ua.exercise.domain.models.data.StartExerciseTransaction
import vm.words.ua.exercise.domain.models.data.WordCompleted

class ExerciseStatisticalManagerImpl : ExerciseStatisticalManager {
    override suspend fun startExercise(data: StartExerciseTransaction) {

    }

    override suspend fun completeWord(data: WordCompleted) {

    }

    override suspend fun endExercise(data: EndExerciseTransaction) {

    }
}