package vm.words.ua.utils.net.managers.impls

import dev.jordond.connectivity.Connectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.utils.net.managers.ConnectivityManager

class JordondConnectivityManager(
    private val connectivity: Connectivity,
    private val scope: CoroutineScope,
) : ConnectivityManager {

    private val _status = MutableStateFlow<Connectivity.Status?>(null)
    val status: StateFlow<Connectivity.Status?> = _status

    override val isOnline: StateFlow<Boolean> =
        MutableStateFlow(false).also { boolFlow ->
            scope.launch {
                connectivity.statusUpdates.collect { s ->
                    _status.value = s
                    boolFlow.value = s is Connectivity.Status.Connected
                }
            }
        }

    init {
        connectivity.start()
    }

    suspend fun checkOnce(): Boolean =
        connectivity.status() is Connectivity.Status.Connected

    fun stop() {
        connectivity.stop()
    }
}