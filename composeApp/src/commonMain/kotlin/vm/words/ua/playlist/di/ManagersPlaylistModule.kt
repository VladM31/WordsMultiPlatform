package vm.words.ua.playlist.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.playlist.domain.managers.PinWordPlayListManager
import vm.words.ua.playlist.domain.managers.PlayListManager
import vm.words.ua.playlist.domain.managers.impl.PinWordWordPlayListManagerImpl
import vm.words.ua.playlist.domain.managers.impl.PlayListManagerImpl


internal val managersPlaylistModule = DI.Module("managersPlaylistModule"){
    // Managers ManagersPlaylistModule
    bind<PlayListManager>() with singleton {
        PlayListManagerImpl(
            playListClient = instance(),
            userCacheManager = instance<UserCacheManager>()
        )
    }

    bind<PinWordPlayListManager>() with singleton {
        PinWordWordPlayListManagerImpl(
            pinWordPlayListClient = instance(),
            userCacheManager = instance<UserCacheManager>()
        )
    }
}