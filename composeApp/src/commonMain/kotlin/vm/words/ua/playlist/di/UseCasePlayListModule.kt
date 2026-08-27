package vm.words.ua.playlist.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import vm.words.ua.playlist.domain.usecases.PinPlayListUseCase
import vm.words.ua.playlist.domain.usecases.UpPinPlayListUseCase


internal val useCasePlayListModule = DI.Module("UseCasePlayListModule") {
    // Managers ManagersPlaylistModule
    bind<PinPlayListUseCase>() with provider {
        PinPlayListUseCase(
            pinPlayListClient = instance(),
            userCacheManager = instance()
        )
    }

    bind<UpPinPlayListUseCase>() with provider {
        UpPinPlayListUseCase(
            pinPlayListClient = instance(),
            userCacheManager = instance()
        )
    }
}