package vm.words.ua.exercise.di

import org.kodein.di.DI

/**
 * Главный модуль Exercise приложения
 */
val exerciseModule = DI.Module("exercise") {
    import(exerciseViewModelModule)
}

