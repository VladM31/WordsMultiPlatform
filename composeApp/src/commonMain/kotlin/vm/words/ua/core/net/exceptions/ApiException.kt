package vm.words.ua.core.net.exceptions

/**
 * Ошибка ответа бэкенда.
 *
 * Auth и user сервисы отдают ошибки простым текстом (`text/plain`),
 * поэтому [body] это готовое сообщение. Для 429 бэк добавляет заголовок `Retry-After` (секунды).
 */
class ApiException(
    val status: Int,
    val body: String,
    val retryAfterSeconds: Long? = null
) : Exception(buildMessage(status, body, retryAfterSeconds)) {

    val isUnauthorized: Boolean get() = status == 401
    val isNotFound: Boolean get() = status == 404
    val isConflict: Boolean get() = status == 409
    val isTooManyRequests: Boolean get() = status == 429
    val isServiceUnavailable: Boolean get() = status == 503

    companion object {
        private const val SECONDS_IN_MINUTE = 60L

        private fun buildMessage(status: Int, body: String, retryAfterSeconds: Long?): String {
            return when (status) {
                401 -> "Session expired. Please sign in again"
                429 -> "Too many attempts. Try again ${formatWait(retryAfterSeconds)}"
                else -> body.trim().ifBlank { "Request failed ($status)" }
            }
        }

        private fun formatWait(seconds: Long?): String {
            if (seconds == null || seconds <= 0) {
                return "later"
            }
            if (seconds < SECONDS_IN_MINUTE) {
                return "in $seconds sec"
            }
            val minutes = (seconds + SECONDS_IN_MINUTE - 1) / SECONDS_IN_MINUTE
            return "in $minutes min"
        }
    }
}
