package vm.words.ua.utils.net

import dev.jordond.connectivity.Connectivity
import kotlin.time.Duration.Companion.seconds

actual fun createConnectivity(): Connectivity =
    Connectivity {
        autoStart = true
        urls("cloudflare.com", "study-words.com")
        port = 443
        pollingIntervalMs = 15.seconds
        timeoutMs = 5.seconds
    }
