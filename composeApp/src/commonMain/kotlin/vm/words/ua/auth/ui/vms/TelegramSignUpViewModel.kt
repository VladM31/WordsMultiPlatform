package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.managers.ContactRequestStatus
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import vm.words.ua.auth.domain.managers.user
import vm.words.ua.auth.domain.models.SignUpModel
import vm.words.ua.auth.ui.actions.TelegramSignUpAction
import vm.words.ua.auth.ui.states.TelegramSignUpState
import vm.words.ua.auth.ui.validation.telegramSignUpValidator
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.utils.toNumbersOnly

class TelegramSignUpViewModel(
    private val authManager: AuthManager,
    private val analytics: Analytics,
    private val telegramWebAppManager: TelegramWebAppManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        TelegramSignUpState(
            firstName = telegramWebAppManager.user()?.firstName.orEmpty(),
            lastName = telegramWebAppManager.user()?.lastName.orEmpty(),
        )
    )
    val state: StateFlow<TelegramSignUpState> = mutableState
    private val validator = telegramSignUpValidator(state)

    fun sent(action: TelegramSignUpAction) {
        when (action) {
            is TelegramSignUpAction.SetPhoneNumber -> {
                mutableState.value = state.value.copy(phoneNumber = action.value.toNumbersOnly())
            }

            is TelegramSignUpAction.SetPassword -> {
                mutableState.value = state.value.copy(password = action.value)
            }

            is TelegramSignUpAction.SetFirstName -> {
                mutableState.value = state.value.copy(firstName = action.value)
            }

            is TelegramSignUpAction.SetLastName -> {
                mutableState.value = state.value.copy(lastName = action.value)
            }

            is TelegramSignUpAction.SetCurrency -> {
                mutableState.value = state.value.copy(currency = action.value)
            }


            TelegramSignUpAction.Submit -> handleSubmit()
            is TelegramSignUpAction.SetAgreed -> {
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


            handleRequestContact()

            authManager.signUp(state.value.toModel()).let {
                val erMes = it.message?.run { ErrorMessage(message = this) }

                mutableState.value = state.value.copy(success = it.success, error = erMes)
                if (it.success) {
                    analytics.logEvent(
                        AnalyticsEvents.SIGNUP_SUCCESS, mapOf(
                            "phone_number" to state.value.phoneNumber,
                            "currency" to state.value.currency.name
                        )
                    )
                    return@let
                }
                analytics.logEvent(
                    AnalyticsEvents.SIGNUP_FAILED, mapOf(
                        "phone_number" to state.value.phoneNumber,
                        "currency" to state.value.currency.name,
                        "error_message" to (it.message ?: "unknown_error")
                    )
                )
            }
        }.invokeOnCompletion {
            if (mutableState.value.success) {
                return@invokeOnCompletion
            }
            val message = it?.message ?: "Unknown error"
            mutableState.value =
                state.value.copy(error = ErrorMessage(message = message))
        }
    }

    private suspend fun handleRequestContact() {
        if (telegramWebAppManager.isAvailable.not()) {
            return
        }
        mutableState.value =
            state.value.copy(error = ErrorMessage(message = "Please allow access to your contact information in Telegram settings and try again"))
        val result = telegramWebAppManager.requestContact()

        when (result) {
            is ContactRequestStatus.Sent -> {
                mutableState.value =
                    state.value.copy(error = ErrorMessage(message = "Please allow access to your contact information in Telegram settings and try again"))
            }

            is ContactRequestStatus.Cancelled -> {
                mutableState.value =
                    state.value.copy(error = ErrorMessage(message = "Go out from Mini App, allow access to your contact information in Telegram bot and try again"))
            }

            is ContactRequestStatus.Unavailable -> {
                mutableState.value =
                    state.value.copy(error = ErrorMessage(message = "Unable to request contact. Please allow access to your contact information in Telegram settings and try again"))
            }
        }
    }

    private fun TelegramSignUpState.toModel() = SignUpModel(
        phoneNumber = phoneNumber,
        password = password,
        firstName = firstName,
        lastName = lastName,
        currency = currency.name
    )
}