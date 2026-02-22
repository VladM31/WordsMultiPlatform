package vm.words.ua.exercise.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vm.words.ua.di.rememberInstance
import vm.words.ua.exercise.domain.managers.ExerciseStatisticalManager
import vm.words.ua.exercise.domain.mappers.toScreen
import vm.words.ua.exercise.domain.models.data.EndExerciseTransaction
import vm.words.ua.exercise.ui.bundles.ExerciseBundle
import vm.words.ua.exercise.ui.bundles.ExerciseResultBundle
import vm.words.ua.exercise.ui.bundles.WordGradeResult
import vm.words.ua.exercise.ui.states.ExerciseState
import vm.words.ua.navigation.Screen
import vm.words.ua.navigation.SimpleNavController

@Composable
@NonRestartableComposable
fun EndExerciseEffect(
    state: ExerciseState,
    bundle: ExerciseBundle,
    navController: SimpleNavController,
) {
    val exerciseStatisticalManager: ExerciseStatisticalManager = rememberInstance()

    LaunchedEffect(state.isEnd) {
        if (state.isEnd.not()) return@LaunchedEffect
        if (state.transactionId != bundle.transactionId) return@LaunchedEffect
        if (state.exercise != bundle.currentExercise) return@LaunchedEffect

        // max grade depends on exercise type (mirrors gradeMapper in ExerciseStatisticalManagerImpl)
        val maxGrade = when (state.exercise) {
            vm.words.ua.exercise.domain.models.enums.Exercise.WORD_BY_TRANSLATES,
            vm.words.ua.exercise.domain.models.enums.Exercise.WORD_BY_ORIGINALS,
            vm.words.ua.exercise.domain.models.enums.Exercise.WORD_BY_DESCRIPTIONS,
            vm.words.ua.exercise.domain.models.enums.Exercise.DESCRIPTION_BY_WORDS -> 1

            else -> 3
        }

        // Build WordGradeResult for this exercise from state.grades (mirrors toWordCompleted logic)
        val currentResults = state.words.mapIndexed { index, word ->
            val grade = state.grades.getOrElse(index) { 0 }
            WordGradeResult(
                wordId = word.wordId,
                original = word.original,
                translate = word.translate,
                grade = grade,
                maxGrade = maxGrade,
                isCorrect = grade > 0,
                exerciseId = state.exercise.id,
            )
        }

        if (bundle.isLast.not()) {
            val newBundle = bundle.toNext(state.words, currentResults)
            bundle.nextExercise.toScreen().let { nextExercise ->
                navController.navigateAndClearCurrent(nextExercise, newBundle)
            }
            return@LaunchedEffect
        }

        withContext(Dispatchers.Default) {
            exerciseStatisticalManager.endExercise(
                EndExerciseTransaction(transactionId = state.transactionId)
            )
        }
        val allResults = bundle.accumulatedResults + currentResults
        val resultBundle = ExerciseResultBundle(
            wordResults = allResults,
            exercises = bundle.exercises.map { it.exercise },
        )
        navController.navigateAndClearCurrent(Screen.ExerciseResult, resultBundle)
    }

}
