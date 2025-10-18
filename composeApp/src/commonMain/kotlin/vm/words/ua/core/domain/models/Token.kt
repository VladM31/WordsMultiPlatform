package vm.words.ua.core.domain.models

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable


@Serializable
data class Token(
    val value : String,
    val expirationTime : Long
){

    // Expired if current time has passed expirationTime
    fun isExpired(now: Long = Clock.System.now().toEpochMilliseconds()) = now >= expirationTime

    // Optional helper: will expire within threshold (e.g., DAY)
    fun willExpireWithin(thresholdMillis: Long, now: Long = Clock.System.now().toEpochMilliseconds()): Boolean =
        (expirationTime - now) <= thresholdMillis

    companion object{
        const val DAY: Long = 24L * 60 * 60 * 1000L
        val DEFAULT = Token("", 0L)
    }
}