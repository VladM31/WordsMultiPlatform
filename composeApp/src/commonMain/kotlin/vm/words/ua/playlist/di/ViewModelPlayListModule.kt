package vm.words.ua.playlist.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.playlist.ui.vms.PlayListViewModel
import vm.words.ua.playlist.ui.vms.PlayListFilterViewModel

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
}