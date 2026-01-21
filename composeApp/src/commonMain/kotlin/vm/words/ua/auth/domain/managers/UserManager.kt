package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.DeleteOptions

interface UserManager {

    suspend fun delete(options: DeleteOptions): Boolean
}