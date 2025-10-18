package vm.words.ua.auth.domain.managers

interface AuthHistoryManager {
    val lastPhoneNumber: String?

    fun updateLastPhoneNumber(phoneNumber: String)

}