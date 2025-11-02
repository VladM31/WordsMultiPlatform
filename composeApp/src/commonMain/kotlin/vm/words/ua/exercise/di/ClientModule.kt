package vm.words.ua.exercise.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.exercise.net.clients.ExerciseStatisticalClient
import vm.words.ua.exercise.net.clients.impls.KrotExerciseStatisticalClient


internal val clientExerciseModule = DI.Module("clientExerciseModule") {
    bind<ExerciseStatisticalClient>() with singleton {
        KrotExerciseStatisticalClient(
            client = instance()
        )
    }
}
