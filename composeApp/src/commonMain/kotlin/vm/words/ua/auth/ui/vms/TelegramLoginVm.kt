package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.AuthHistoryManager
import vm.words.ua.auth.domain.managers.TelegramAuthManager
import vm.words.ua.auth.domain.models.TelegramAuthSession
import vm.words.ua.auth.ui.actions.TelegramLoginAction
import vm.words.ua.auth.ui.states.TelegramLoginState
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.utils.toNumbersOnly
import vm.words.ua.utils.validation.Validator
import vm.words.ua.utils.validation.schemes.ValidationScheme
import vm.words.ua.utils.validation.schemes.isPhoneNumber

class TelegramLoginVm(
    private val telegramAuthManager: TelegramAuthManager,
    private val authHistoryManager: AuthHistoryManager,
    private val analytics: Analytics
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        TelegramLoginState(
            phoneNumber = authHistoryManager.lastUsername.orEmpty()
        )
    )
    val state: StateFlow<TelegramLoginState> = mutableState
    private val validator = Validator(state)

    init {
        validator.add(
            { it.phoneNumber },
            ValidationScheme.stringSchema("Phone number")
                .isPhoneNumber()
        )
        telegramAuthManager.session?.let {
            mutableState.value = mutableState.value.copy(
                phoneNumber = it.phoneNumber,
                code = it.code,
                isLoading = false
            )
            viewModelScope.launch(Dispatchers.Default) {
                runWaiter(it)
            }
        }
    }

    fun sent(action: TelegramLoginAction) {
        when (action) {
            is TelegramLoginAction.SetPhoneNumber -> handlePhoneNumber(action)
            is TelegramLoginAction.Submit -> handleSubmit()
            is TelegramLoginAction.CheckLogin -> handleCheckLogin()
        }
    }

    private fun handleCheckLogin() {
        viewModelScope.launch(Dispatchers.Default) {
            val success =
                telegramAuthManager.login(mutableState.value.phoneNumber, mutableState.value.code)
            if (success) {
                authHistoryManager.updateLastUsername(mutableState.value.phoneNumber)
                mutableState.value = mutableState.value.copy(isEnd = true)
            }
        }
    }

    private fun handlePhoneNumber(action: TelegramLoginAction.SetPhoneNumber) {
        mutableState.value = mutableState.value.copy(phoneNumber = action.value.toNumbersOnly())
    }

    private fun handleSubmit() {
        val message = validator.validate()
        if (message.isNotBlank()) {
            mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(message))
            return
        }

        viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = mutableState.value.copy(isLoading = true)

            val result = telegramAuthManager.runCatching {
                startLogin(mutableState.value.phoneNumber)
            }

            if (result.isFailure) {
                analytics.logEvent(
                    AnalyticsEvents.TELEGRAM_LOGIN_FAILED, mapOf(
                        "phone_number" to mutableState.value.phoneNumber,
                        "error" to (result.exceptionOrNull()?.message ?: "Unknown error")
                    )
                )
                mutableState.value = mutableState.value.copy(
                    errorMessage = ErrorMessage(
                        result.exceptionOrNull()?.message ?: "Unknown error"
                    ),
                    isLoading = false
                )
                return@launch
            }

            mutableState.value = mutableState.value.copy(
                code = result.getOrNull().orEmpty(),
                isLoading = false
            )

            runWaiter(
                TelegramAuthSession(
                    phoneNumber = mutableState.value.phoneNumber,
                    code = mutableState.value.code
                )
            )
        }
    }

    private suspend fun runWaiter(session: TelegramAuthSession) {
        while (true) {
            val success = telegramAuthManager.login(
                session.phoneNumber,
                session.code
            )
            if (success) {
                authHistoryManager.updateLastUsername(session.phoneNumber)
                mutableState.value = mutableState.value.copy(isEnd = true)
                analytics.logEvent(
                    AnalyticsEvents.TELEGRAM_LOGIN_SUCCESS, mapOf(
                        "phone_number" to session.phoneNumber
                    )
                )
                break
            }
            delay(3000)
        }
    }
}