package vm.words.ua.auth.domain.factories

import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.models.google.GoogleSignInResult

/**
 * iOS implementation of GoogleSignInService
 *
 * NOTE: Full Google Sign-In on iOS requires:
 * 1. Add GoogleSignIn pod to iosApp/Podfile
 * 2. Configure URL schemes in Info.plist
 * 3. Implement native Swift/ObjC wrapper
 *
 * Currently returns stub implementation.
 * To enable: integrate Google Sign-In SDK via CocoaPods
 */
class GoogleApiManagerIos : GoogleApiManager {

    override fun isAvailable(): Boolean {
        // Google Sign-In is not yet implemented for iOS
        // Return false to hide the button on iOS
        return false
    }

    override suspend fun signIn(): GoogleSignInResult {
        return GoogleSignInResult.failure(
            "Google Sign-In is not yet available on iOS. " +
                    "Please use phone number or Telegram login."
        )
    }

    override suspend fun signOut() {
        // No-op for stub implementation
    }
}

/**
 * Factory function for iOS platform
 */
actual fun createGoogleSignInManager(): GoogleApiManager = GoogleApiManagerIos()

