package vm.words.ua.utils.net

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.compose.ConnectivityState
import dev.jordond.connectivity.compose.rememberConnectivityState
import vm.words.ua.di.rememberInstance


expect fun createConnectivity(): Connectivity

@Composable
fun rememberAppConnectivityState(): ConnectivityState {
    val connectivity = rememberInstance<Connectivity>()

    val state = rememberConnectivityState(connectivity)

    LaunchedEffect(Unit) {
        state.startMonitoring()
    }

    return state
}

@Composable
fun hasInternet(): Boolean {
    return rememberAppConnectivityState().isConnected
}
