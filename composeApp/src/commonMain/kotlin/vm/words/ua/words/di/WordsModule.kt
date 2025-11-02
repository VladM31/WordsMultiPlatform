package vm.words.ua.words.di

import org.kodein.di.DI

val wordsModule = DI.Module("wordsModule") {
    import(clientWordsModule)
    import(managersWordsModule)
    import(viewModelWordsModule)
}