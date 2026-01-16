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
import vm.words.ua.exercise.ui.states.ExerciseState
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
        if (state.exercise != bundle.currentExercise)return@LaunchedEffect
        if (bundle.isLast) {
            withContext(Dispatchers.Default) {
                exerciseStatisticalManager.endExercise(
                    EndExerciseTransaction(transactionId = state.transactionId)
                )

            }
            navController.popBackStack()
            return@LaunchedEffect
        }
        val newBundle = bundle.toNext(state.words)
        bundle.nextExercise.toScreen().let { nextExercise ->
            navController.navigateAndClearCurrent(nextExercise, newBundle)
        }
    }

}
