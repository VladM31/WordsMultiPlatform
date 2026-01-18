package vm.words.ua.auth.domain.factories

import vm.words.ua.auth.domain.managers.GoogleSignInManager
import vm.words.ua.auth.domain.models.GoogleSignInResult

/**
 * Desktop (JVM) implementation of GoogleSignInService
 *
 * Google Sign-In is not available on desktop platforms.
 * Desktop users should use phone number or Telegram login.
 */
class GoogleSignInManagerDesktop : GoogleSignInManager {

    override fun isAvailable(): Boolean = false

    override suspend fun signIn(): GoogleSignInResult {
        return GoogleSignInResult.failure(
            "Google Sign-In is not available on desktop. " +
                    "Please use phone number or Telegram login."
        )
    }

    override suspend fun signOut() {
        // No-op for desktop
    }
}

/**
 * Factory function for Desktop (JVM) platform
 */
actual fun createGoogleSignInManager(): GoogleSignInManager = GoogleSignInManagerDesktop()

