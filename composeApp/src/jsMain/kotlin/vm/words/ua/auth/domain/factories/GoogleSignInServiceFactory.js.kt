package vm.words.ua.auth.domain.factories

import kotlinx.coroutines.await
import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.models.google.GoogleSignInResult
import kotlin.js.Promise

/**
 * JS (Browser) implementation of GoogleSignInManager using Firebase Auth
 */
class GoogleApiManagerJs : GoogleApiManager {

    override fun isAvailable(): Boolean {
        return js("typeof window.isGoogleSignInAvailable === 'function' && window.isGoogleSignInAvailable()") as Boolean
    }

    override suspend fun signIn(): GoogleSignInResult {
        return try {
            val resultJs: dynamic = signInWithGoogleJs().await()

            val success = resultJs.success == true

            if (success) {
                val email = (resultJs.email as? String) ?: ""
                val userId = (resultJs.userId as? String) ?: ""
                val displayName = resultJs.displayName as? String
                val idToken = resultJs.idToken as? String

                GoogleSignInResult.success(
                    email = email,
                    displayName = displayName?.takeIf { it.isNotEmpty() },
                    idToken = idToken?.takeIf { it.isNotEmpty() }
                )
            } else {
                val error = (resultJs.error as? String) ?: "Unknown error"
                GoogleSignInResult.failure(error)
            }
        } catch (e: Exception) {
            GoogleSignInResult.failure("Google Sign-In failed: ${e.message}")
        }
    }

    override suspend fun signOut() {
        try {
            signOutFromGoogleJs().await()
        } catch (e: Exception) {
            println("Sign out error: ${e.message}")
        }
    }
}

private fun signInWithGoogleJs(): Promise<dynamic> = js("window.signInWithGoogle()")

private fun signOutFromGoogleJs(): Promise<dynamic> = js("window.signOutFromGoogle()")

/**
 * Factory function for JS platform
 */
actual fun createGoogleSignInManager(): GoogleApiManager = GoogleApiManagerJs()

