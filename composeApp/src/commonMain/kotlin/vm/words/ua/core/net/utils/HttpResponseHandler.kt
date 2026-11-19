package vm.words.ua.core.net.utils

import io.ktor.client.statement.*
import io.ktor.http.*
import vm.words.ua.core.net.exceptions.ApiException

/**
 * Бросает [ApiException], если статус ответа не 2xx.
 * Тело ошибки читается как простой текст, `Retry-After` (для 429) как секунды.
 */
suspend fun HttpResponse.throwIfError(): HttpResponse {
    if (this.status.isSuccess()) {
        return this
    }
    throw ApiException(
        status = this.status.value,
        body = runCatching { this.bodyAsText() }.getOrDefault(""),
        retryAfterSeconds = this.headers[HttpHeaders.RetryAfter]?.toLongOrNull()
    )
}
