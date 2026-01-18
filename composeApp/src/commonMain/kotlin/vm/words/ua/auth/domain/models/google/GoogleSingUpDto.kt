package vm.words.ua.auth.domain.models.google

import vm.words.ua.core.domain.models.enums.Currency

data class GoogleSingUpDto(
    val password: String,
    val firstName: String,
    val lastName: String,
    val currency: Currency
)
