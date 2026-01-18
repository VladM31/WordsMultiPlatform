@file:OptIn(ExperimentalWasmJsInterop::class)
@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

package vm.words.ua.auth.domain.factories

import kotlinx.coroutines.await
import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.models.google.GoogleSignInResult
import kotlin.js.Promise

/**
 * WasmJS (Browser) implementation of GoogleSignInManager using Firebase Auth
 */
class GoogleApiManagerWasm : GoogleApiManager {

    override fun isAvailable(): Boolean {
        return isGoogleSignInAvailableJs()
    }

    override suspend fun signIn(): GoogleSignInResult {
        return try {
            val resultJs = signInWithGoogleJs().await<JsAny>()

            val success = getSuccess(resultJs)

            if (success) {
                val email = getEmail(resultJs)
                val displayName = getDisplayName(resultJs)
                val idToken = getIdToken(resultJs)

                GoogleSignInResult.success(
                    email = email,
                    displayName = displayName.takeIf { it.isNotEmpty() },
                    idToken = idToken.takeIf { it.isNotEmpty() }
                )
            } else {
                val error = getError(resultJs)
                GoogleSignInResult.failure(error)
            }
        } catch (e: Exception) {
            GoogleSignInResult.failure("Google Sign-In failed: ${e.message}")
        }
    }

    override suspend fun signOut() {
        try {
            signOutFromGoogleJs().await<JsAny>()
        } catch (e: Exception) {
            println("Sign out error: ${e.message}")
        }
    }
}

// External JS interop functions using @JsFun
@JsFun("() => typeof window.isGoogleSignInAvailable === 'function' && window.isGoogleSignInAvailable()")
private external fun isGoogleSignInAvailableJs(): Boolean

@JsFun("() => window.signInWithGoogle()")
private external fun signInWithGoogleJs(): Promise<JsAny>

@JsFun("() => window.signOutFromGoogle()")
private external fun signOutFromGoogleJs(): Promise<JsAny>

@JsFun("(result) => result.success === true")
private external fun getSuccess(result: JsAny): Boolean

@JsFun("(result) => result.email || ''")
private external fun getEmail(result: JsAny): String

@JsFun("(result) => result.userId || ''")
private external fun getUserId(result: JsAny): String

@JsFun("(result) => result.displayName || ''")
private external fun getDisplayName(result: JsAny): String

@JsFun("(result) => result.idToken || ''")
private external fun getIdToken(result: JsAny): String

@JsFun("(result) => result.error || 'Unknown error'")
private external fun getError(result: JsAny): String

/**
 * Factory function for WasmJS platform
 */
actual fun createGoogleApiManager(): GoogleApiManager = GoogleApiManagerWasm()


