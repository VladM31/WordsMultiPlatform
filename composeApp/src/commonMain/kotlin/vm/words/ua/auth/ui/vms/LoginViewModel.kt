package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.AuthHistoryManager
import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.models.AuthResult
import vm.words.ua.auth.ui.actions.LoginAction
import vm.words.ua.auth.ui.states.LoginState
import vm.words.ua.auth.ui.validation.loginValidator
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.ui.models.ErrorMessage

class LoginViewModel(
    private val authManager: AuthManager,
    private val authHistoryManager: AuthHistoryManager,
    private val analytics: Analytics
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        LoginState(
            phoneNumber = authHistoryManager.lastPhoneNumber.orEmpty()
        )
    )
    val state: StateFlow<LoginState> = mutableState
    private val validator = loginValidator(mutableState)

    fun sent(action: LoginAction) {
        when (action) {
            is LoginAction.Submit -> submit()
            is LoginAction.SetPhoneNumber -> setPhoneNumber(action)
            is LoginAction.SetPassword -> setPassword(action)
        }
    }

    private fun setPassword(action: LoginAction.SetPassword) {
        mutableState.value = mutableState.value.copy(password = action.value)
    }

    private fun setPhoneNumber(action: LoginAction.SetPhoneNumber) {
        mutableState.value = mutableState.value.copy(phoneNumber = action.value, isAvailableBiometric = false)

    }

    private fun submit() {
        val errors = validator.validate(" - ")

        if (errors.isNotEmpty()) {
            mutableState.value = state.value.copy(errorMessage = ErrorMessage(message = errors))
            return
        }

        viewModelScope.launch(Dispatchers.Default) {
            logIn()
        }
    }

    private suspend fun logIn() {
        try {
            val result = authManager.logIn(
                phoneNumber = state.value.phoneNumber,
                password = state.value.password
            )

            val error = result.message?.let {
                ErrorMessage(message = it)
            }

            if (result.success) {
                authHistoryManager.updateLastPhoneNumber(state.value.phoneNumber)
            }

            emitEvent(result)

            mutableState.value = state.value.copy(
                isEnd = result.success,
                errorMessage = error
            )
        } catch (e: Exception) {
            mutableState.value =
                state.value.copy(errorMessage = ErrorMessage(message = e.message ?: "Error"))
        }
    }

    private fun emitEvent(result: AuthResult) {
        val eventName = if (result.success) {
            AnalyticsEvents.LOGIN_SUCCESS
        } else {
            AnalyticsEvents.LOGIN_FAILED
        }
        analytics.logEvent(eventName, mapOf("phone_number" to state.value.phoneNumber))
    }
}