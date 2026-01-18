package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import vm.words.ua.auth.domain.services.GoogleSignInService
import vm.words.ua.auth.domain.services.createGoogleSignInService

/**
 * DI module for Google Sign-In Service
 */
val googleSignInDi = DI.Module("googleSignIn") {
    bindSingleton<GoogleSignInService> { createGoogleSignInService() }
}

