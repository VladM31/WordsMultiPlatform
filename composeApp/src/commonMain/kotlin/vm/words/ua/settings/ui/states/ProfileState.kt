package vm.words.ua.settings.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState

data class ProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String? = null,
    val phoneNumber: String? = null,
    val tryToDelete: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
) : ErrorableState
