package vm.words.ua.exercise.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.exercise.ui.vm.ExerciseSelectionViewModel
import vm.words.ua.exercise.ui.vm.MatchWordsViewModel
import vm.words.ua.exercise.ui.vm.SelectingAnOptionVm

/**
 * Модуль Kodein-DI для ViewModels Exercise
 */
internal val exerciseViewModelModule = DI.Module("exerciseViewModel") {
    bind<ExerciseSelectionViewModel>() with factory {
        ExerciseSelectionViewModel(
            subscribeCacheManager = instance(),
            byteContentManager = instance()
        )
    }
    bind<SelectingAnOptionVm>() with factory {
        SelectingAnOptionVm(
            soundManager = instance(),
            contentManager = instance()
        )
    }

    bind<MatchWordsViewModel>() with factory {
        MatchWordsViewModel()
    }
}

