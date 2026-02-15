package vm.words.ua.auth.domain.factories

import kotlinx.cinterop.ExperimentalForeignApi
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.models.google.GoogleSignInResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class GoogleApiManagerIos : GoogleApiManager {

    override fun isAvailable(): Boolean {
        return GoogleSignInHelperBridge.isConfigured()
    }

    override suspend fun signIn(): GoogleSignInResult {
        return try {
            GoogleSignInHelperBridge.signIn()
        } catch (e: Exception) {
            GoogleSignInResult.failure(
                "Google Sign-In error: ${e.message}"
            )
        }
    }

    override suspend fun signOut() {
        try {
            GoogleSignInHelperBridge.signOut()
        } catch (e: Exception) {
            // Ignore sign-out errors
        }
    }
}


object GoogleSignInHelperBridge {

    private var helperConfigured = false

    private var signInHandler: ((callback: (Map<String, Any?>) -> Unit) -> Unit)? = null
    private var signOutHandler: (() -> Unit)? = null
    private var isAvailableHandler: (() -> Boolean)? = null


    fun configure(
        signIn: (callback: (Map<String, Any?>) -> Unit) -> Unit,
        signOut: () -> Unit,
        isAvailable: () -> Boolean
    ) {
        signInHandler = signIn
        signOutHandler = signOut
        isAvailableHandler = isAvailable
        helperConfigured = true
    }

    /**
     * Check if helper is configured
     */
    fun isConfigured(): Boolean = helperConfigured && (isAvailableHandler?.invoke() ?: false)

    /**
     * Perform sign-in
     */
    @OptIn(ExperimentalForeignApi::class)
    suspend fun signIn(): GoogleSignInResult = suspendCoroutine { continuation ->
        dispatch_async(dispatch_get_main_queue()) {
            val handler = signInHandler
            if (handler == null) {
                continuation.resume(
                    GoogleSignInResult.failure(
                        "GoogleSignInHelper not configured. " +
                        "Please add GoogleSignIn SDK in Xcode and configure the bridge at app startup."
                    )
                )
                return@dispatch_async
            }

            try {
                handler { resultMap ->
                    val success = resultMap["success"] as? Boolean ?: false
                    if (success) {
                        continuation.resume(
                            GoogleSignInResult.success(
                                email = resultMap["email"] as? String ?: "",
                                displayName = resultMap["displayName"] as? String,
                                idToken = resultMap["idToken"] as? String
                            )
                        )
                    } else {
                        continuation.resume(
                            GoogleSignInResult.failure(
                                resultMap["error"] as? String ?: "Unknown error"
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                continuation.resume(
                    GoogleSignInResult.failure("Sign-in failed: ${e.message}")
                )
            }
        }
    }

    /**
     * Perform sign-out
     */
    fun signOut() {
        signOutHandler?.invoke()
    }
}

/**
 * Factory function for iOS platform
 */
actual fun createGoogleApiManager(): GoogleApiManager = GoogleApiManagerIos()
