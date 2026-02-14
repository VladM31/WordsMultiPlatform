package vm.words.ua.utils.net

import dev.jordond.connectivity.Connectivity
import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual fun createConnectivity(): Connectivity {
    // Explicitly create HttpClient to use HTTP-based connectivity (not device-based)
    val httpClient = HttpClient(Darwin)

    return Connectivity(httpClient = httpClient) {
        autoStart = true
        urls("cloudflare.com", "study-words.com")
        port = 443
        pollingIntervalMs = 15.seconds
        timeoutMs = 5.seconds
    }
}
