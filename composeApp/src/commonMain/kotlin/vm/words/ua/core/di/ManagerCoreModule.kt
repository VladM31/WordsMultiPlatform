package vm.words.ua.core.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import vm.words.ua.core.domain.crypto.TokenCipherFactory
import vm.words.ua.core.domain.managers.SettingsFactory
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.managers.impl.SharedUserCacheManager

internal val managerCoreModule = DI.Module("managerCoreModule") {
    bind<UserCacheManager>() with singleton {
        SharedUserCacheManager(
            settings = SettingsFactory.create(),
            tokenCipher = TokenCipherFactory.create()
        )
    }
}