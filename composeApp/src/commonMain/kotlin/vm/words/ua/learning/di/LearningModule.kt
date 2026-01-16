package vm.words.ua.learning.di

import org.kodein.di.DI

val learningModule = DI.Module("LearningModule") {
    import(clientLearningModule)
    import(learningManagerDi)
    import(learningViewModelDi)
}