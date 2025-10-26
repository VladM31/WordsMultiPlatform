package vm.words.ua.words.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import vm.words.ua.words.domain.managers.SoundManager
import vm.words.ua.words.domain.managers.impl.SoundManagerImpl

internal val managersWordsModule = DI.Module("managersWordsModule") {
    bind<SoundManager>() with singleton {
        SoundManagerImpl()
    }
}

