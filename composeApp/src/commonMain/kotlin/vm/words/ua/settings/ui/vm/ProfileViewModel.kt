package vm.words.ua.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import vm.words.ua.auth.domain.managers.TelegramWebAppManager
import vm.words.ua.auth.domain.managers.UserManager
import vm.words.ua.auth.domain.models.DeleteOptions
import vm.words.ua.auth.domain.models.OwnershipProof
import vm.words.ua.auth.domain.models.OwnershipPurpose
import vm.words.ua.auth.domain.models.enums.TelegramCodeStatus
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.settings.ui.actions.ProfileAction
import vm.words.ua.settings.ui.states.ProfileDialog
import vm.words.ua.settings.ui.states.ProfileState
import vm.words.ua.settings.ui.validations.validateName
import vm.words.ua.settings.ui.validations.validatePassword

class ProfileViewModel(
    private val userCacheManager: UserCacheManager,
    private val userManager: UserManager,
    private val telegramWebAppManager: TelegramWebAppManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(createState())

    /** Ожидание нажатия Yes в боте, его прерывают закрытие диалога и новый запрос подтверждения */
    private var proofJob: Job? = null

    private fun createState(): ProfileState = if (userCacheManager.isExpired) {
        ProfileState(
            firstName = "",
            lastName = "",
            email = "",
            phoneNumber = "",
            errorMessage = ErrorMessage(message = "Session expired. Please log in again.")
        )
    } else {
        ProfileState(
            firstName = userCacheManager.user.firstName,
            lastName = userCacheManager.user.lastName,
            email = userCacheManager.user.email,
            phoneNumber = userCacheManager.user.phoneNumber,
            hasPassword = userCacheManager.user.hasPassword,
            // как и на экране входа: внутри Telegram Mini App Google недоступен
            isGoogleAvailable = userManager.isGoogleAvailable && telegramWebAppManager.isAvailable.not(),
            // внутри Mini App подтверждение идёт по initData, номер телефона не нужен
            isTelegramProofAvailable = telegramWebAppManager.isAvailable ||
                    !userCacheManager.user.phoneNumber.isNullOrBlank()
        )
    }

    val state: StateFlow<ProfileState> = mutableState


    fun sent(action: ProfileAction) {
        when (action) {
            is ProfileAction.OpenDialog -> {
                proofJob?.cancel()
                mutableState.value = state.value.copy(
                    dialog = action.dialog,
                    proof = null,
                    telegramProofCode = null,
                    errorMessage = null
                )
            }

            is ProfileAction.DismissDialog -> closeDialog()
            is ProfileAction.DismissError -> mutableState.value = state.value.copy(errorMessage = null)
            is ProfileAction.DismissMessage -> mutableState.value = state.value.copy(message = null)
            is ProfileAction.SaveName -> handleSaveName(action)
            is ProfileAction.SetPassword -> handleSetPassword(action)
            is ProfileAction.DeleteAccount -> handleDeleteAccount(action)
            is ProfileAction.LinkGoogle -> handleLinkGoogle()
            is ProfileAction.ProveWithGoogle -> handleProveWithGoogle()
            is ProfileAction.ProveWithTelegram -> handleProveWithTelegram(action)
        }
    }

    private fun closeDialog() {
        proofJob?.cancel()
        mutableState.value = state.value.copy(
            dialog = ProfileDialog.NONE,
            proof = null,
            telegramProofCode = null,
            errorMessage = null
        )
    }

    private fun handleSaveName(action: ProfileAction.SaveName) {
        val error = validateName("First name", action.firstName)
            ?: validateName("Last name", action.lastName)
        if (error != null) {
            showError(error)
            return
        }

        request {
            userManager.editName(action.firstName, action.lastName)
            val user = userCacheManager.user
            mutableState.value = state.value.copy(
                firstName = user.firstName,
                lastName = user.lastName,
                dialog = ProfileDialog.NONE,
                message = "Name updated"
            )
        }
    }

    private fun handleSetPassword(action: ProfileAction.SetPassword) {
        if (isTelegramConfirmationPending()) return
        val proof = state.value.proof
        // Подтверждение через Google или Telegram главнее: поле текущего пароля при этом игнорируется,
        // иначе введённый там мусор или автозаполнение отбросит proof (код Telegram останется неиспользованным)
        val oldPassword = if (proof != null) null else action.oldPassword?.takeIf { it.isNotEmpty() }

        val error = validatePassword("New password", action.newPassword)
            ?: oldPassword?.let { validatePassword("Current password", it) }
            ?: if (oldPassword == null && proof == null) {
                "Enter your current password or confirm ownership with Google or Telegram"
            } else {
                null
            }
        if (error != null) {
            showError(error)
            return
        }

        request { setPassword(action.newPassword, oldPassword, proof) }
    }

    /** Для смены достаточно oldPassword, proof нужен для первого или забытого пароля */
    private suspend fun setPassword(newPassword: String, oldPassword: String?, proof: OwnershipProof?) {
        userManager.setPassword(
            newPassword = newPassword,
            oldPassword = oldPassword,
            proof = proof
        )
        closeDialog()
        mutableState.value = state.value.copy(hasPassword = true, message = "Password updated")
    }

    private fun handleDeleteAccount(action: ProfileAction.DeleteAccount) {
        if (isTelegramConfirmationPending()) return
        val proof = state.value.proof
        // proof главнее пароля, по той же причине, что и при смене пароля
        val password = if (proof != null) null else action.password?.takeIf { it.isNotEmpty() }

        val error = password?.let { validatePassword("Password", it) }
            ?: if (password == null && proof == null) {
                "Enter your password or confirm ownership with Google or Telegram"
            } else {
                null
            }
        if (error != null) {
            showError(error)
            return
        }

        request {
            userManager.delete(
                DeleteOptions(
                    password = password,
                    proof = proof,
                    reason = action.reason
                )
            )
            userCacheManager.clear()
        }
    }

    private fun handleLinkGoogle() {
        request {
            userManager.linkGoogle()
            val user = userCacheManager.user
            mutableState.value = state.value.copy(
                email = user.email,
                message = "Google account linked"
            )
        }
    }

    private fun handleProveWithGoogle() {
        request {
            val proof = userManager.requestGoogleProof()
            mutableState.value = state.value.copy(proof = proof, telegramProofCode = null)
        }
    }

    /**
     * Подтверждение владения через Telegram, без лишних кнопок:
     * - внутри Mini App хватает подписанного `initData`, бот не нужен;
     * - иначе бот присылает сообщение, а приложение само ждёт Yes (опрашивает статус кода).
     * Для смены пароля новый пароль отправляется сразу после подтверждения.
     */
    private fun handleProveWithTelegram(action: ProfileAction.ProveWithTelegram) {
        if (state.value.isLoading || state.value.telegramProofCode != null) return

        val dialog = state.value.dialog
        if (dialog == ProfileDialog.PASSWORD) {
            // Проверяем заранее: после подтверждения пароль уйдёт сам, исправить его уже не получится
            val error = validatePassword("New password", action.newPassword.orEmpty())
            if (error != null) {
                showError(error)
                return
            }
        }
        val newPassword = action.newPassword.takeIf { dialog == ProfileDialog.PASSWORD }

        proofJob?.cancel()
        proofJob = viewModelScope.launch(Dispatchers.Default) {
            if (telegramWebAppManager.isAvailable) {
                execute { onProofReady(OwnershipProof.miniApp(telegramWebAppManager.initData), newPassword) }
                return@launch
            }
            runTelegramProof(dialog, newPassword)
        }
    }

    private suspend fun runTelegramProof(dialog: ProfileDialog, newPassword: String?) {
        // Текст сообщения в боте зависит от того, что подтверждаем: пароль или удаление
        val purpose = when (dialog) {
            ProfileDialog.DELETE -> OwnershipPurpose.DELETE_ACCOUNT
            else -> OwnershipPurpose.SET_PASSWORD
        }
        var code: String? = null
        execute {
            code = userManager.requestTelegramProofCode(purpose)
            mutableState.value = state.value.copy(telegramProofCode = code, proof = null)
        }
        val requested = code ?: return

        val deadline = Clock.System.now().plus(PROOF_LIFETIME_MINUTES, DateTimeUnit.MINUTE)
        while (Clock.System.now() < deadline) {
            val status = try {
                userManager.telegramProofStatus(requested)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // нет сети или бэк отвечает с ошибкой: ждём дальше
                delay(POLL_DELAY_MILLIS)
                continue
            }

            when (status) {
                TelegramCodeStatus.PENDING -> delay(POLL_DELAY_MILLIS)
                TelegramCodeStatus.CONFIRMED -> {
                    execute { onProofReady(OwnershipProof.telegram(requested), newPassword) }
                    return
                }

                TelegramCodeStatus.REFUSED -> return stopWaiting("You pressed No in Telegram. Nothing was changed")
                TelegramCodeStatus.USED -> return stopWaiting("This confirmation was already used. Try again")
                TelegramCodeStatus.EXPIRED -> return stopWaiting("The confirmation expired. Try again")
            }
        }
        stopWaiting("Nothing was confirmed in Telegram in time. Try again")
    }

    /**
     * Подтверждение получено. Смена пароля завершается сама, для удаления пользователь нажимает "Delete":
     * необратимое действие не должно уходить от одного "Yes" в боте.
     */
    private suspend fun onProofReady(proof: OwnershipProof, newPassword: String?) {
        mutableState.value = state.value.copy(proof = proof, telegramProofCode = null)
        if (state.value.dialog == ProfileDialog.PASSWORD && newPassword != null) {
            setPassword(newPassword, oldPassword = null, proof = proof)
        }
    }

    private fun stopWaiting(message: String) {
        mutableState.value = state.value.copy(
            telegramProofCode = null,
            errorMessage = ErrorMessage(message = message)
        )
    }

    /** Основное действие диалога во время ожидания нажимать рано: подтверждения ещё нет */
    private fun isTelegramConfirmationPending(): Boolean {
        if (state.value.telegramProofCode == null) return false
        showError("Waiting for your Yes in the Telegram bot")
        return true
    }

    private fun showError(message: String) {
        mutableState.value = state.value.copy(errorMessage = ErrorMessage(message = message))
    }

    /**
     * Выполняет запрос к бэку с индикатором загрузки.
     * Текст ошибки бэка (неверный пароль, 429 с временем ожидания и т.п.) показывается как есть.
     */
    private fun request(block: suspend () -> Unit) {
        if (state.value.isLoading) return
        viewModelScope.launch(Dispatchers.Default) {
            execute(block)
        }
    }

    private suspend fun execute(block: suspend () -> Unit) {
        mutableState.value = state.value.copy(isLoading = true, errorMessage = null)
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            showError(e.message ?: "Unknown error")
        } finally {
            mutableState.value = state.value.copy(isLoading = false)
        }
    }

    private companion object {
        /** Код входа на бэке действует 15 минут */
        const val PROOF_LIFETIME_MINUTES = 15
        const val POLL_DELAY_MILLIS = 2000L
    }
}
