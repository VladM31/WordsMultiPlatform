package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.google.GoogleSignInResult


interface GoogleApiManager {

    fun isAvailable(): Boolean

    suspend fun signIn(): GoogleSignInResult

    suspend fun signOut()
}