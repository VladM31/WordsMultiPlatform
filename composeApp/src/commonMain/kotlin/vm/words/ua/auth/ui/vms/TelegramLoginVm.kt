package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import vm.words.ua.auth.domain.managers.AuthHistoryManager
import vm.words.ua.auth.domain.managers.ContactRequestStatus
import vm.words.ua.auth.domain.managers.TelegramAuthManager
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import vm.words.ua.auth.domain.models.TelegramAuthResult
import vm.words.ua.auth.domain.models.TelegramAuthSession
import vm.words.ua.auth.domain.models.enums.TelegramMiniAppLoginStatus
import vm.words.ua.auth.ui.actions.TelegramLoginAction
import vm.words.ua.auth.ui.states.TelegramLoginState
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.net.exceptions.ApiException
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.utils.toNumbersOnly
import vm.words.ua.utils.validation.Validator
import vm.words.ua.utils.validation.schemes.ValidationScheme
import vm.words.ua.utils.validation.schemes.isPhoneNumber

class TelegramLoginVm(
    private val telegramAuthManager: TelegramAuthManager,
    private val authHistoryManager: AuthHistoryManager,
    private val analytics: Analytics,
    private val telegramWebAppManager: TelegramWebAppManager,
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        TelegramLoginState(
            phoneNumber = authHistoryManager.lastUsername.orEmpty(),
            isMiniApp = telegramWebAppManager.isAvailable
        )
    )
    val state: StateFlow<TelegramLoginState> = mutableState
    private val validator = Validator(state)

    /** Опрос подтверждения входа, его можно прервать через [TelegramLoginAction.Cancel] */
    private var waiterJob: Job? = null

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
            waiterJob = viewModelScope.launch(Dispatchers.Default) {
                runWaiter(it)
            }
        }
    }

    private fun handleCancel() {
        waiterJob?.cancel()
        waiterJob = null
        telegramAuthManager.cancelSession()
        mutableState.value = mutableState.value.copy(
            code = "",
            isLoading = false,
            errorMessage = null
        )
    }

    fun sent(action: TelegramLoginAction) {
        when (action) {
            is TelegramLoginAction.SetPhoneNumber -> handlePhoneNumber(action)
            is TelegramLoginAction.Submit -> handleSubmit()
            is TelegramLoginAction.CheckLogin -> handleCheckLogin()
            is TelegramLoginAction.SubmitMiniApp -> handleSubmitMiniApp()
            is TelegramLoginAction.Cancel -> handleCancel()
        }
    }

    private fun handleSubmitMiniApp() {
        mutableState.value = mutableState.value.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.Default) {
            val initData = telegramWebAppManager.initData
            val status = telegramAuthManager.loginWithMiniApp(initData)

            if (status == TelegramMiniAppLoginStatus.SUCCESS) {
                handleMiniSussesLogin()
                return@launch
            }

            // ERROR: подпись неверна, данные устарели или бот недоступен
            if (status == TelegramMiniAppLoginStatus.ERROR) {
                handleFail(
                    Result.failure(
                        Exception("Telegram sign-in failed: the data is invalid or expired, or the bot is unavailable. Close and reopen the app, then try again.")
                    )
                )
                return@launch
            }

            // NOT_FOUND_IN_BOT: бот не знает телефон пользователя, нужно открыть бота и отправить контакт
            mutableState.value = mutableState.value.copy(
                isLoading = false,
                errorMessage = ErrorMessage("The Telegram bot doesn't know your phone number. Open the bot, send your contact and try again.")
            )
            val contactStatus = telegramWebAppManager.requestContact()
            val message = when (contactStatus) {
                ContactRequestStatus.Sent -> "Contact request sent. Please try logging in again after sharing your contact."
                ContactRequestStatus.Cancelled -> "Contact request cancelled. Please confirm your phone number in Telegram and try again."
                else -> {
                    null
                }
            }
            message?.let {
                mutableState.value = mutableState.value.copy(
                    errorMessage = ErrorMessage(it)
                )
            }
        }.invokeOnCompletion {
            if (it != null) {
                handleFail(Result.failure(it))
                return@invokeOnCompletion
            }
            mutableState.value = mutableState.value.copy(
                isLoading = false
            )
        }
    }

    private fun handleMiniSussesLogin() {
        mutableState.value = mutableState.value.copy(isEnd = true)
        analytics.logEvent(
            AnalyticsEvents.TELEGRAM_LOGIN_SUCCESS, mapOf(
                "phone_number" to mutableState.value.phoneNumber,
                "method" to "mini_app"
            )
        )
    }

    private fun handleCheckLogin() {
        viewModelScope.launch(Dispatchers.Default) {
            val result = runCatching {
                telegramAuthManager.login(mutableState.value.phoneNumber, mutableState.value.code)
            }
            result.exceptionOrNull()?.let {
                mutableState.value = mutableState.value.copy(
                    errorMessage = ErrorMessage(it.message ?: "Unknown error")
                )
                return@launch
            }
            if (result.getOrDefault(false)) {
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

        waiterJob = viewModelScope.launch(Dispatchers.Default) {
            mutableState.value = mutableState.value.copy(isLoading = true)

            val result = telegramAuthManager.runCatching {
                startLogin(mutableState.value.phoneNumber)
            }

            if (result.isFailure) {
                handleFail(result)
                return@launch
            }
            if (result.getOrNull()?.isNotFound == true) {
                handleNotFound()
                return@launch
            }

            handleWaitAction(result)
        }
    }

    private suspend fun handleWaitAction(result: Result<TelegramAuthResult>) {
        mutableState.value = mutableState.value.copy(
            code = result.getOrNull()?.code.orEmpty(),
            isLoading = false
        )

        runWaiter(
            TelegramAuthSession(
                phoneNumber = mutableState.value.phoneNumber,
                code = mutableState.value.code
            )
        )
    }

    private fun handleNotFound() {
        mutableState.value = mutableState.value.copy(
            isLoading = false,
            isNotFoundId = Clock.System.now().toEpochMilliseconds()
        )
        analytics.logEvent(
            AnalyticsEvents.TELEGRAM_LOGIN_FAILED, mapOf(
                "phone_number" to mutableState.value.phoneNumber,
                "error" to "User with this phone number not found"
            )
        )
    }

    private fun handleFail(result: Result<TelegramAuthResult>) {
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
    }

    /**
     * Опрашивает `POST /auth/telegram-login` каждые 3 секунды, пока пользователь не нажмёт кнопку в боте.
     * Код живёт 15 минут, после этого ожидание прекращается. После успеха код одноразовый,
     * повторный вызов вернёт `success=false`.
     */
    private suspend fun runWaiter(session: TelegramAuthSession) {
        val deadline = Clock.System.now().plus(CODE_LIFETIME_MINUTES, DateTimeUnit.MINUTE)
        while (Clock.System.now() < deadline) {
            val success = try {
                telegramAuthManager.login(
                    session.phoneNumber,
                    session.code
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: ApiException) {
                when {
                    // лимит запросов: ждём столько, сколько просит бэк
                    e.isTooManyRequests -> {
                        delay((e.retryAfterSeconds ?: POLL_DELAY_SECONDS) * 1000)
                        continue
                    }
                    // бот временно недоступен: продолжаем опрос
                    e.isServiceUnavailable -> {
                        delay(POLL_DELAY_SECONDS * 1000)
                        continue
                    }
                    // аккаунт не активен, код неизвестен и т.п.: ждать нет смысла
                    else -> {
                        stopWaiting(e.message ?: "Telegram login failed")
                        return
                    }
                }
            } catch (e: Exception) {
                // нет сети и подобное: пробуем ещё раз
                delay(POLL_DELAY_SECONDS * 1000)
                continue
            }
            if (success) {
                authHistoryManager.updateLastUsername(session.phoneNumber)
                mutableState.value = mutableState.value.copy(isEnd = true)
                analytics.logEvent(
                    AnalyticsEvents.TELEGRAM_LOGIN_SUCCESS, mapOf(
                        "phone_number" to session.phoneNumber
                    )
                )
                return
            }
            delay(POLL_DELAY_SECONDS * 1000)
        }
        stopWaiting("Login was not confirmed in Telegram in time. Please try again")
    }

    private fun stopWaiting(message: String) {
        telegramAuthManager.cancelSession()
        analytics.logEvent(
            AnalyticsEvents.TELEGRAM_LOGIN_FAILED, mapOf(
                "phone_number" to mutableState.value.phoneNumber,
                "error" to message
            )
        )
        mutableState.value = mutableState.value.copy(
            code = "",
            isLoading = false,
            errorMessage = ErrorMessage(message)
        )
    }

    private companion object {
        /** Код входа на бэке действует 15 минут */
        const val CODE_LIFETIME_MINUTES = 15
        const val POLL_DELAY_SECONDS = 3L
    }
}