package vm.words.ua.settings.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vm.words.ua.auth.domain.managers.UserManager
import vm.words.ua.auth.domain.models.DeleteOptions
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.settings.ui.actions.ProfileAction
import vm.words.ua.settings.ui.states.ProfileState
import vm.words.ua.settings.ui.validations.profileDeleteValidator

class ProfileViewModel(
    private val userCacheManager: UserCacheManager,
    private val userManager: UserManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(createState())

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
        )
    }

    val state: StateFlow<ProfileState> = mutableState


    fun sent(action: ProfileAction) {
        when (action) {
            is ProfileAction.DeleteAccount -> handleDeleteAccount(action)
            is ProfileAction.OpenDeleteAccountDialog -> {
                mutableState.value = state.value.copy(
                    tryToDelete = true
                )
            }

            is ProfileAction.DismissDeleteAccountDialog -> {
                mutableState.value = state.value.copy(
                    tryToDelete = false,
                    errorMessage = null
                )
            }
        }
    }

    private fun handleDeleteAccount(action: ProfileAction.DeleteAccount) {
        val validator = profileDeleteValidator(MutableStateFlow(action))
        val result = validator.validate("-")
        if (result.isNotBlank()) {
            mutableState.value = state.value.copy(
                errorMessage = ErrorMessage(message = result)
            )
            return
        }

        viewModelScope.launch(Dispatchers.Default) {

            try {
                val result = userManager.delete(
                    DeleteOptions(
                        password = action.password,
                        reason = action.reason
                    )
                )
                if (result.not()) {
                    mutableState.value = state.value.copy(
                        errorMessage = ErrorMessage(message = "Failed to delete account")
                    )
                    return@launch
                }
                userCacheManager.clear()


            } catch (ex: Throwable) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage(message = ex.message ?: "Unknown error")
                )
            }

        }

    }
}