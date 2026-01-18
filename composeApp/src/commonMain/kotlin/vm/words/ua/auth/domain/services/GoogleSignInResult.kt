package vm.words.ua.auth.domain.services

/**
 * Result of Google Sign-In operation
 */
data class GoogleSignInResult(
    val success: Boolean,
    val email: String? = null,
    val userId: String? = null,
    val displayName: String? = null,
    val idToken: String? = null,
    val errorMessage: String? = null
) {
    companion object {
        fun success(
            email: String,
            userId: String,
            displayName: String? = null,
            idToken: String? = null
        ) = GoogleSignInResult(
            success = true,
            email = email,
            userId = userId,
            displayName = displayName,
            idToken = idToken
        )

        fun failure(errorMessage: String) = GoogleSignInResult(
            success = false,
            errorMessage = errorMessage
        )

        fun notAvailable() = GoogleSignInResult(
            success = false,
            errorMessage = "Google Sign-In is not available on this platform"
        )
    }
}

