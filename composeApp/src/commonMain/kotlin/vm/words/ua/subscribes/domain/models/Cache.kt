package vm.words.ua.subscribes.domain.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Cache<T>(
    val value: T?,
    val cacheDate: Instant
) {
    val isEmpty: Boolean
        get() = value == null

    val isPresent: Boolean
        get() = value != null

    val durationSeconds: Long
        get() = (Clock.System.now() - cacheDate).inWholeSeconds

    companion object {
        fun <T> ofNow(value: T): Cache<T?> {
            return Cache(
                value,
                Clock.System.now()
            )
        }

        fun <T> empty(): Cache<T?> {
            return Cache(
                null,
                Clock.System.now()
            )
        }
    }
}

