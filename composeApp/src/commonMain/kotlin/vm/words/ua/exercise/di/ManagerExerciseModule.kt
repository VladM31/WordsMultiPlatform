package vm.words.ua.exercise.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.exercise.domain.managers.ExerciseStatisticalManager
import vm.words.ua.exercise.domain.managers.impls.ExerciseStatisticalManagerImpl

internal val managerExerciseModule = DI.Module("managerExerciseModule") {
    bind<ExerciseStatisticalManager>() with singleton {
        ExerciseStatisticalManagerImpl(
            learningHistoryClient = instance(),
            learningPlanClient = instance(),
            userManager = instance(),
            client = instance()
        )
    }

}