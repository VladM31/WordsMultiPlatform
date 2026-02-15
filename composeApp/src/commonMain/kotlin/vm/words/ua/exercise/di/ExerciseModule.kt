package vm.words.ua.exercise.di

import org.kodein.di.DI


val exerciseModule = DI.Module("exercise") {
    import(clientExerciseModule)
    import(managerExerciseModule)
    import(exerciseViewModelModule)
}

