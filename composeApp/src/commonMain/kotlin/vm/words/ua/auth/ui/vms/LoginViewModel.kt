package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.exceptions.GoogleLoginException
import vm.words.ua.auth.domain.managers.*
import vm.words.ua.auth.domain.models.AuthResult
import vm.words.ua.auth.domain.models.GoogleLoginErrorMessage
import vm.words.ua.auth.domain.models.google.GmailLoginDto
import vm.words.ua.auth.ui.actions.LoginAction
import vm.words.ua.auth.ui.states.LoginState
import vm.words.ua.auth.ui.validation.gmailLoginValidator
import vm.words.ua.auth.ui.validation.loginValidator
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.ui.models.ErrorMessage

class LoginViewModel(
    private val authManager: AuthManager,
    private val authHistoryManager: AuthHistoryManager,
    private val analytics: Analytics,
    private val googleAuthManager: GoogleAuthManager,
    telegramAuthManager: TelegramAuthManager,
    googleApiManager: GoogleApiManager,
    //    val manager = rememberInstance<TelegramWebAppManager>()
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        LoginState(
            username = authHistoryManager.lastUsername.orEmpty(),
            isGoogleSignInAvailable = googleApiManager.isAvailable(),
            telegramLoginSession = telegramAuthManager.session
        )
    )
    val state: StateFlow<LoginState> = mutableState
    private val phoneNumberValidator = loginValidator(mutableState)
    private val emailValidator = gmailLoginValidator(mutableState)

    fun sent(action: LoginAction) {
        when (action) {
            is LoginAction.Submit -> submit()
            is LoginAction.SetUsername -> setUsername(action)
            is LoginAction.SetPassword -> setPassword(action)
            is LoginAction.GoogleSignIn -> handleGoogleSignIn()
            is LoginAction.DismissErrorMessage -> mutableState.value = state.value.copy(errorMessage = null)
            is LoginAction.ClearNotFound -> mutableState.value = state.value.copy(isNotFoundGoogle = false)
        }
    }

    private fun handleGoogleSignIn() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val result = googleAuthManager.login()
                val error = result.message?.let {
                    if (result.isNotFound) {
                        null
                    } else {
                        ErrorMessage(message = it)
                    }
                }

                emitEvent(result.success)

                mutableState.value = state.value.copy(
                    isEnd = result.success,
                    errorMessage = error,
                    isNotFoundGoogle = result.isNotFound
                )
            } catch (e: GoogleLoginException) {
                mutableState.value =
                    state.value.copy(errorMessage = GoogleLoginErrorMessage(message = e.message ?: "Error"))
            } catch (e: Exception) {
                mutableState.value =
                    state.value.copy(errorMessage = ErrorMessage(message = e.message ?: "Error"))
            }
        }
    }

    private fun setPassword(action: LoginAction.SetPassword) {
        mutableState.value = mutableState.value.copy(password = action.value)
    }

    private fun setUsername(action: LoginAction.SetUsername) {

        mutableState.value = mutableState.value.copy(username = action.value, isAvailableBiometric = false)

    }

    private fun submit() {
        val errors = if (state.value.isPhone) {
            phoneNumberValidator.validate(" - ")
        } else {
            emailValidator.validate(" - ")
        }

        if (errors.isNotEmpty()) {
            mutableState.value = state.value.copy(errorMessage = ErrorMessage(message = errors))
            return
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (state.value.isPhone) {
                phoneNumberLogIn()
            } else {
                gmailLogIn()
            }
        }
    }

    private suspend fun gmailLogIn() {
        val dto = GmailLoginDto(
            email = state.value.username,
            password = state.value.password
        )
        handleResult(googleAuthManager.login(dto))


    }

    private fun handleResult(result: AuthResult) {
        val error = result.message?.let {
            ErrorMessage(message = it)
        }

        if (result.success) {
            authHistoryManager.updateLastUsername(state.value.username)
        }

        emitEvent(result.success)

        mutableState.value = state.value.copy(
            isEnd = result.success,
            errorMessage = error
        )
    }

    private suspend fun phoneNumberLogIn() {
        try {
            val result = authManager.logIn(
                phoneNumber = state.value.username,
                password = state.value.password
            )
            handleResult(result)
        } catch (e: Exception) {
            mutableState.value =
                state.value.copy(errorMessage = ErrorMessage(message = e.message ?: "Error"))
        }
    }

    private fun emitEvent(success: Boolean) {
        val eventName = if (success) {
            AnalyticsEvents.LOGIN_SUCCESS
        } else {
            AnalyticsEvents.LOGIN_FAILED
        }
        analytics.logEvent(eventName, mapOf("phone_number" to state.value.username))
    }
}