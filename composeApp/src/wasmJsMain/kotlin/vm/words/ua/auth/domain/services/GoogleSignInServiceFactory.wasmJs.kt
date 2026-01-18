package vm.words.ua.auth.domain.services

/**
 * WasmJS (Browser) implementation of GoogleSignInService
 *
 * Google Sign-In could be implemented via Firebase JS SDK or Google Identity Services.
 * Currently returns stub implementation.
 */
class GoogleSignInServiceWasm : GoogleSignInService {

    override fun isAvailable(): Boolean = false

    override suspend fun signIn(): GoogleSignInResult {
        return GoogleSignInResult.failure(
            "Google Sign-In is not yet available on web. " +
                    "Please use phone number or Telegram login."
        )
    }

    override suspend fun signOut() {
        // No-op for WasmJS stub
    }
}

/**
 * Factory function for WasmJS platform
 */
actual fun createGoogleSignInService(): GoogleSignInService = GoogleSignInServiceWasm()

