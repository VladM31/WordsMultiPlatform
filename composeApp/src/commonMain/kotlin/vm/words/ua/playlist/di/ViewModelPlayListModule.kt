package vm.words.ua.playlist.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.playlist.ui.vms.*

internal val viewModelPlayListModule = DI.Module("viewModelPlayListModule"){
    // ViewModels
    bind<PlayListViewModel>() with factory {
        PlayListViewModel(
            playListManager = instance()
        )
    }

    bind<PlayListFilterViewModel>() with factory {
        PlayListFilterViewModel()
    }

    bind<PlayListDetailsViewModel>() with factory {
        PlayListDetailsViewModel(
            playListManager = instance(),
            pinPlayListManager = instance()
        )
    }

    bind<ExplorePlayListsViewModel>() with factory {
        ExplorePlayListsViewModel(
            playListManager = instance()
        )
    }

    bind<ExplorePlayListsFilterViewModel>() with factory {
        ExplorePlayListsFilterViewModel()
    }

    bind<PublicPlayListDetailsViewModel>() with factory {
        PublicPlayListDetailsViewModel(
            playListManager = instance()
        )
    }

    bind<FastStartPlayListVm>() with factory {
        FastStartPlayListVm(
            playListManager = instance()
        )
    }
}