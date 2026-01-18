package vm.words.ua.auth.ui.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.models.AuthResult
import vm.words.ua.auth.ui.actions.ConfirmSignUpAction
import vm.words.ua.auth.ui.states.ConfirmSignUpState
import vm.words.ua.core.ui.models.ErrorMessage

class ConfirmSignUpVm(
    private val authManager: AuthManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(ConfirmSignUpState())
    val state: StateFlow<ConfirmSignUpState> = mutableState


    fun sent(action: ConfirmSignUpAction) {
        when (action) {
            is ConfirmSignUpAction.Init -> handleInit(action)
        }
    }

    private fun handleInit(action: ConfirmSignUpAction.Init) {
        if (state.value.isInited()) return

        mutableState.value = state.value.copy(
            phoneNumber = action.phoneNumber,
            password = action.password,
        )
        waitConfirmRegistration()

    }

    private fun waitConfirmRegistration() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                while (authManager.isRegistered(state.value.phoneNumber).not()) {
                    delay(5000)
                }

                var result: AuthResult? = null

                while (result?.success != true) {
                    try {
                        result = authManager.logIn(
                            phoneNumber = state.value.phoneNumber,
                            password = state.value.password
                        )
                    } catch (e: Exception) {
                        delay(3000)
                    }
                }

                mutableState.value = state.value.copy(waitResult = false)
            } catch (e: Exception) {
                mutableState.value = state.value.copy(
                    waitResult = false,
                    error = ErrorMessage(e.message.orEmpty())
                )
            }

        }
    }
}