package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.GoogleAuthManager
import vm.words.ua.auth.domain.models.google.GoogleSingUpDto
import vm.words.ua.auth.ui.actions.GoogleSignUpAction
import vm.words.ua.auth.ui.states.GoogleSignUpState
import vm.words.ua.auth.ui.validation.googleSignUpValidator
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.ui.models.ErrorMessage

class GoogleSignUpViewModel(
    private val googleAuthManager: GoogleAuthManager,
    private val analytics: Analytics
) : ViewModel() {

    private val mutableState = MutableStateFlow(GoogleSignUpState())
    val state: StateFlow<GoogleSignUpState> = mutableState
    private val validator = googleSignUpValidator(state)

    fun sent(action: GoogleSignUpAction) {
        when (action) {
            is GoogleSignUpAction.SetPassword -> {
                mutableState.value = state.value.copy(password = action.value)
            }

            is GoogleSignUpAction.SetFirstName -> {
                mutableState.value = state.value.copy(firstName = action.value)
            }

            is GoogleSignUpAction.SetLastName -> {
                mutableState.value = state.value.copy(lastName = action.value)
            }

            is GoogleSignUpAction.SetCurrency -> {
                mutableState.value = state.value.copy(currency = action.value)
            }

            is GoogleSignUpAction.SetAgreed -> {
                mutableState.value = state.value.copy(agreed = action.value)
            }

            GoogleSignUpAction.Submit -> handleSubmit()

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
                googleAuthManager.signUp(state.value.toDto()).let {
                    val erMes = it.message?.run { ErrorMessage(message = this) }

                    mutableState.value = state.value.copy(success = it.success, error = erMes)
                    if (it.success) {
                        analytics.logEvent(
                            AnalyticsEvents.SIGNUP_SUCCESS, mapOf(
                                "type" to "gmail",
                                "currency" to state.value.currency.name
                            )
                        )
                        return@let
                    }
                    analytics.logEvent(
                        AnalyticsEvents.SIGNUP_FAILED, mapOf(
                            "type" to "gmail",
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

    private fun GoogleSignUpState.toDto() = GoogleSingUpDto(
        password = password,
        firstName = firstName,
        lastName = lastName,
        currency = currency
    )
}