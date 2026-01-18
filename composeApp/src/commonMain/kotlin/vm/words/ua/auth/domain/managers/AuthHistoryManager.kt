package vm.words.ua.auth.domain.managers

interface AuthHistoryManager {
    val lastUsername: String?

    fun updateLastUsername(username: String)

}