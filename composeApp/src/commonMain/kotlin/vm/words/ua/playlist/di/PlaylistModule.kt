package vm.words.ua.playlist.di

import org.kodein.di.DI


val playlistModule = DI.Module("playlist") {
    import(clientsPlaylistModule)
    import(managersPlaylistModule)
    import(useCasePlayListModule)
    import(viewModelPlayListModule)
}

