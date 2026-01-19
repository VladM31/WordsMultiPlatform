package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.google.GoogleSignInResult

/**
 * Service for Google Sign-In across all platforms
 */
interface GoogleApiManager {
    /**
     * Check if Google Sign-In is available on this platform
     */
    fun isAvailable(): Boolean

    /**
     * Perform Google Sign-In
     * @return GoogleSignInResult with user data on success or error message on failure
     */
    suspend fun signIn(): GoogleSignInResult

    /**
     * Sign out from Google account
     */
    suspend fun signOut()
}