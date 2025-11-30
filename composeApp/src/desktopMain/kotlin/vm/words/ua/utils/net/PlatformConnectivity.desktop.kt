package vm.words.ua.utils.net

import dev.jordond.connectivity.Connectivity

actual fun createConnectivity(): Connectivity =
    Connectivity {
        autoStart = true
        urls("cloudflare.com", "study-words.com")
        port = 80
        pollingIntervalMs = 10.minutes
        timeoutMs = 5.seconds
    }