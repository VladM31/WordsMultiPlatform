package vm.words.ua.words.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.words.domain.managers.SoundManager
import vm.words.ua.words.domain.managers.UserWordManager
import vm.words.ua.words.domain.managers.WordManager
import vm.words.ua.words.domain.managers.impl.SoundManagerImpl
import vm.words.ua.words.domain.managers.impl.UserWordManagerImpl
import vm.words.ua.words.domain.managers.impl.WordManagerImpl

internal val managersWordsModule = DI.Module("managersWordsModule") {
    bind<SoundManager>() with singleton {
        SoundManagerImpl()
    }

    bind<WordManager>() with singleton {
        WordManagerImpl(
            wordClient = instance(),
            userCacheManager = instance()
        )
    }

    bind<UserWordManager>() with singleton {
        UserWordManagerImpl(
            userWordClient = instance(),
            userCacheManager = instance(),
            fileClient = instance()
        )
    }
}

