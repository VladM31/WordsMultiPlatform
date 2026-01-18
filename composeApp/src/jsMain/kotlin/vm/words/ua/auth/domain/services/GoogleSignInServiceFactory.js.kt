package vm.words.ua.auth.domain.services

/**
 * JS (Browser) implementation of GoogleSignInService
 *
 * Google Sign-In could be implemented via Firebase JS SDK or Google Identity Services.
 * Currently returns stub implementation.
 */
class GoogleSignInServiceJs : GoogleSignInService {

    override fun isAvailable(): Boolean = false

    override suspend fun signIn(): GoogleSignInResult {
        return GoogleSignInResult.failure(
            "Google Sign-In is not yet available on web. " +
                    "Please use phone number or Telegram login."
        )
    }

    override suspend fun signOut() {
        // No-op for JS stub
    }
}

/**
 * Factory function for JS platform
 */
actual fun createGoogleSignInService(): GoogleSignInService = GoogleSignInServiceJs()

