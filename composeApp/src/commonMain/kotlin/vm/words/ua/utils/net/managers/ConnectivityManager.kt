package vm.words.ua.utils.net.managers

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityManager {

    val isOnline: StateFlow<Boolean>
}