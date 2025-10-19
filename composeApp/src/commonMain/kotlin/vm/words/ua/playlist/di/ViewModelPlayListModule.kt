package vm.words.ua.playlist.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import vm.words.ua.playlist.ui.vms.PlayListViewModel

internal val viewModelPlayListModule = DI.Module("viewModelPlayListModule"){
    // ViewModels
    bind<PlayListViewModel>() with factory {
        PlayListViewModel(
            playListManager = instance()
        )
    }
}