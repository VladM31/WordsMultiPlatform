package vm.words.ua.auth.domain.models

data class SignUpModel(
    val phoneNumber: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val currency: String,
    val email: String?
)