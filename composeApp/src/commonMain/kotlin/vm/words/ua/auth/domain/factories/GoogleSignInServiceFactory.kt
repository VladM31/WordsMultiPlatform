package vm.words.ua.auth.domain.factories

import vm.words.ua.auth.domain.managers.GoogleApiManager

/**
 * Expect function to create platform-specific GoogleSignInService implementation
 */
expect fun createGoogleApiManager(): GoogleApiManager

