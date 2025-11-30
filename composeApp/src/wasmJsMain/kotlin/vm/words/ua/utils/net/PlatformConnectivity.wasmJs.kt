package vm.words.ua.utils.net

import dev.jordond.connectivity.Connectivity

actual fun createConnectivity(): Connectivity =
    Connectivity {
        autoStart = true
        urls("study-words.com")
        port = 443
        pollingIntervalMs = 15.seconds
        timeoutMs = 5.seconds
    }