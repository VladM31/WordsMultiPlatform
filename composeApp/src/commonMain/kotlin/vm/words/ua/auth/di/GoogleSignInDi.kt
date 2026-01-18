package vm.words.ua.auth.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import vm.words.ua.auth.domain.factories.createGoogleSignInManager
import vm.words.ua.auth.domain.managers.GoogleSignInManager

/**
 * DI module for Google Sign-In Service
 */
val googleSignInDi = DI.Module("googleSignIn") {
    bindSingleton<GoogleSignInManager> { createGoogleSignInManager() }
}

