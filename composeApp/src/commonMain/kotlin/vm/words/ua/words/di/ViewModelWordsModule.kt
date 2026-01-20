package vm.words.ua.words.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.words.ui.vms.*

internal val viewModelWordsModule = DI.Module("viewModelWordsModule") {
    bind<WordDetailsViewModel>() with factory {
        WordDetailsViewModel(
            byteContentManager = instance(),
            soundManager = instance(),
            subscribeManager = instance(),
            userWordManager = instance()
        )
    }

    bind<WordsViewModel>() with factory {
        WordsViewModel(
            wordManager = instance(),
            userCacheManager = instance()
        )
    }

    bind<WordFilterViewModel>() with factory {
        WordFilterViewModel()
    }

    bind<UserWordsViewModel>() with factory {
        UserWordsViewModel(
            userWordManager = instance(),
            pinPlayListManager = instance(),
            playListManager = instance()
        )
    }

    bind<UserWordFilterVm>() with factory {
        UserWordFilterVm()
    }

    bind<PinUserWordsViewModel>() with factory {
        PinUserWordsViewModel(
            userWordManager = instance(),
            subscribeCacheManager = instance(),
            byteContentManager = instance(),
            soundManager = instance()
        )
    }

    bind<DefaultAddWordVm>() with factory {
        DefaultAddWordVm(
            wordManager = instance(),
            subscribeCacheManager = instance(),
            soundManager = instance()
        )
    }
}
