package vm.words.ua.auth.domain.exceptions

class GoogleNotFoundException(override val message: String?) : GoogleLoginException(message) {
}