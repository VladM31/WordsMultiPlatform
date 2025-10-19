package vm.words.ua.playlist.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vm.words.ua.playlist.ui.vms.PlayListViewModel


val playlistModule = DI.Module("playlist") {
    import(clientsPlaylistModule)
    import(managersPlaylistModule)
    import(viewModelPlayListModule)
}

