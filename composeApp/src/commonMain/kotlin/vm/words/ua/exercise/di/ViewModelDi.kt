package vm.words.ua.exercise.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.exercise.ui.vm.*


internal val exerciseViewModelModule = DI.Module("exerciseViewModel") {
    bind<ExerciseSelectionViewModel>() with factory {
        ExerciseSelectionViewModel(
            subscribeCacheManager = instance(),
            byteContentManager = instance(),
            exerciseStatisticalManager = instance(),
            exerciseRecommendationManager = instance()
        )
    }
    bind<SelectingAnOptionVm>() with factory {
        SelectingAnOptionVm(
            soundManager = instance(),
            contentManager = instance(),
            exerciseStatisticalManager = instance()
        )
    }

    bind<MatchWordsViewModel>() with factory {
        MatchWordsViewModel(
            exerciseStatisticalManager = instance()
        )
    }

    bind<LettersMatchVm>() with factory {
        LettersMatchVm(
            soundManager = instance(),
            contentManager = instance(),
            exerciseStatisticalManager = instance()
        )
    }

    bind<WriteByImageAndFieldVm>() with factory {
        WriteByImageAndFieldVm(
            soundManager = instance(),
            contentManager = instance(),
            exerciseStatisticalManager = instance()
        )
    }
}

