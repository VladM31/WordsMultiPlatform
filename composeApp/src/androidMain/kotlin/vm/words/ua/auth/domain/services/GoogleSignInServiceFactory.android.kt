package vm.words.ua.auth.domain.services

import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import vm.words.ua.auth.domain.models.GoogleSignInResult

/**
 * Android implementation of GoogleSignInService using Credential Manager + Firebase Auth
 */
class GoogleSignInServiceAndroid : GoogleSignInService {

    companion object {
        private const val TAG = "GoogleSignIn"

        /**
         * Web Client ID from Firebase Console:
         * 1. Go to Firebase Console -> Authentication -> Sign-in method
         * 2. Enable Google Sign-In
         * 3. Copy the "Web client ID" (not Android client ID!)
         *
         * The Web Client ID looks like: XXXXX.apps.googleusercontent.com
         */
        private const val WEB_CLIENT_ID = "1005445939198-rlkom295h4c9rmuf4jd43cv87nm9qpsk.apps.googleusercontent.com"

    }

    override fun isAvailable(): Boolean {
        // Google Sign-In is available on Android if we have Activity context and configured
        return ActivityHolder.getActivity() != null
    }

    override suspend fun signIn(): GoogleSignInResult {
        val activity = ActivityHolder.getActivity()
            ?: return GoogleSignInResult.failure("Activity not available")

        return try {
            val credentialManager = CredentialManager.create(activity)

            // First try with authorized accounts only (faster)
            val googleIdOptionAuthorized = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .build()

            val requestAuthorized = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOptionAuthorized)
                .build()

            try {
                val result = credentialManager.getCredential(
                    request = requestAuthorized,
                    context = activity
                )
                return handleSignInResult(result)
            } catch (e: NoCredentialException) {
                Log.d(TAG, "No authorized accounts, trying all accounts...")
            }

            // Fallback: try with all Google accounts
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result: GetCredentialResponse = credentialManager.getCredential(
                request = request,
                context = activity
            )

            handleSignInResult(result)
        } catch (e: GetCredentialCancellationException) {
            Log.d(TAG, "User cancelled sign-in")
            GoogleSignInResult.failure("Sign-in cancelled")
        } catch (e: NoCredentialException) {
            Log.e(TAG, "No credentials available", e)
            GoogleSignInResult.failure(
                "No Google accounts available. " +
                        "Please add a Google account in device Settings, " +
                        "or ensure SHA-1 fingerprint is added to Firebase Console."
            )
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException: ${e.type} - ${e.message}", e)
            GoogleSignInResult.failure("Sign-in failed: ${e.type} - ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during sign-in", e)
            GoogleSignInResult.failure("Unexpected error: ${e.message}")
        }
    }

    private suspend fun handleSignInResult(result: GetCredentialResponse): GoogleSignInResult {
        val credential = result.credential

        return when {
            credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken

                    // Sign in with Firebase using the Google ID token
                    val firebaseCredential = GoogleAuthProvider.credential(idToken, null)
                    val authResult = Firebase.auth.signInWithCredential(firebaseCredential)
                    val user = authResult.user

                    if (user != null) {
                        GoogleSignInResult.success(
                            email = user.email ?: googleIdTokenCredential.id,
                            userId = user.uid,
                            displayName = user.displayName ?: googleIdTokenCredential.displayName,
                            idToken = idToken
                        )
                    } else {
                        GoogleSignInResult.failure("Firebase authentication failed")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing credential", e)
                    GoogleSignInResult.failure("Error processing credential: ${e.message}")
                }
            }

            else -> {
                Log.e(TAG, "Unexpected credential type: ${credential.type}")
                GoogleSignInResult.failure("Unexpected credential type")
            }
        }
    }

    override suspend fun signOut() {
        try {
            Firebase.auth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Error signing out", e)
        }
    }
}

/**
 * Factory function for Android platform
 */
actual fun createGoogleSignInService(): GoogleSignInService = GoogleSignInServiceAndroid()

