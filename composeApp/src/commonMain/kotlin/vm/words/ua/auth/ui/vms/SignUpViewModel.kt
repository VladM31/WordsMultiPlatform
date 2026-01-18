package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.models.SignUpModel
import vm.words.ua.auth.ui.actions.SignUpAction
import vm.words.ua.auth.ui.states.SignUpState
import vm.words.ua.auth.ui.validation.signUpValidator
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.utils.toNumbersOnly

class SignUpViewModel(
    private val authManager: AuthManager,
    private val analytics: Analytics
) : ViewModel() {

    private val mutableState = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = mutableState
    private val validator = signUpValidator(state)

    fun sent(action: SignUpAction) {
        when (action) {
            is SignUpAction.SetPhoneNumber -> {
                mutableState.value = state.value.copy(phoneNumber = action.value.toNumbersOnly())
            }

            is SignUpAction.SetPassword -> {
                mutableState.value = state.value.copy(password = action.value)
            }

            is SignUpAction.SetFirstName -> {
                mutableState.value = state.value.copy(firstName = action.value)
            }

            is SignUpAction.SetLastName -> {
                mutableState.value = state.value.copy(lastName = action.value)
            }

            is SignUpAction.SetCurrency -> {
                mutableState.value = state.value.copy(currency = action.value)
            }

            is SignUpAction.SetEmail -> {
                mutableState.value = state.value.copy(email = action.value)
            }

            SignUpAction.Submit -> handleSubmit()
            is SignUpAction.SetAgreed -> {
                mutableState.value = state.value.copy(agreed = action.value)
            }
        }
    }

    private fun handleSubmit() {
        val errorMessage = validator.validate(" - ")

        if (errorMessage.isNotBlank()) {
            mutableState.value = state.value.copy(error = ErrorMessage(message = errorMessage))
            return
        }
        if (state.value.agreed.not()) {
            mutableState.value = state.value.copy(
                error = ErrorMessage(message = "You must agree to the terms and conditions")
            )
            return
        }

        viewModelScope.launch(Dispatchers.Default) {
            try {
                authManager.signUp(state.value.toModel()).let {
                    val erMes = it.message?.run { ErrorMessage(message = this) }



                    mutableState.value = state.value.copy(success = it.success, error = erMes)
                    if (it.success) {
                        analytics.logEvent(
                            AnalyticsEvents.SIGNUP_SUCCESS, mapOf(
                                "phone_number" to state.value.phoneNumber,
                                "email" to (state.value.email ?: "not_provided"),
                                "currency" to state.value.currency.name
                            )
                        )
                        return@let
                    }
                    analytics.logEvent(
                        AnalyticsEvents.SIGNUP_FAILED, mapOf(
                            "phone_number" to state.value.phoneNumber,
                            "email" to (state.value.email ?: "not_provided"),
                            "currency" to state.value.currency.name,
                            "error_message" to (it.message ?: "unknown_error")
                        )
                    )
                }
            } catch (e: Exception) {
                mutableState.value =
                    state.value.copy(error = ErrorMessage(message = e.message ?: "Unknown error"))
            }
        }
    }

    private fun SignUpState.toModel() = SignUpModel(
        phoneNumber = phoneNumber,
        password = password,
        firstName = firstName,
        lastName = lastName,
        currency = currency.name,
        email = email?.ifBlank { null }
    )
}