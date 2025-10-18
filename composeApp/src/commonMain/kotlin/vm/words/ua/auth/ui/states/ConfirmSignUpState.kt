package  vm.words.ua.auth.ui.states

data class ConfirmSignUpState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val phoneNumber: String = "",
    val password: String = "",
){

    fun isInited() = phoneNumber.isNotEmpty()
}
