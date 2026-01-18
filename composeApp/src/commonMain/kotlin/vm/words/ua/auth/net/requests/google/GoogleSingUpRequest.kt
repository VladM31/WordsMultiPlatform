package vm.words.ua.auth.net.requests.google

import vm.words.ua.core.domain.models.enums.Currency

data class GoogleSingUpRequest(
    val idToken: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val currency: Currency
)