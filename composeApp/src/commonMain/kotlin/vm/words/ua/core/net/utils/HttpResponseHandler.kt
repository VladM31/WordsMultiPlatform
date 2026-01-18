package vm.words.ua.core.net.utils

import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun HttpResponse.throwIfError(): HttpResponse {
    if (this.status.isSuccess()) {
        return this
    }
    throw Exception(this.bodyAsText())
}