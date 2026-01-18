package  vm.words.ua.auth.ui.states

import vm.words.ua.core.domain.models.enums.Currency
import vm.words.ua.core.ui.models.ErrorMessage


data class SignUpState(
    val success: Boolean = false,
    val error: ErrorMessage? = null,
    val phoneNumber: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val currency: Currency = Currency.USD,
    val agreed: Boolean = false
)
