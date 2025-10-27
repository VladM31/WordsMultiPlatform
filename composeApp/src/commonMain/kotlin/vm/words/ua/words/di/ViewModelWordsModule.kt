package vm.words.ua.words.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.words.ui.vms.WordDetailsViewModel

internal val viewModelWordsModule = DI.Module("viewModelWordsModule") {
    bind<WordDetailsViewModel>() with factory {
        WordDetailsViewModel(
            byteContentManager = instance(),
            soundManager = instance(),
            subscribeManager = instance()
        )
    }
}
