package vm.words.ua.exercise.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import vm.words.ua.exercise.ui.vm.ExerciseSelectionViewModel

/**
 * Модуль Kodein-DI для ViewModels Exercise
 */
internal val exerciseViewModelModule = DI.Module("exerciseViewModel") {
    bind<ExerciseSelectionViewModel>() with factory {
        ExerciseSelectionViewModel()
    }
}

