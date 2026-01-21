package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.DeleteAccountRequest

interface UserClient {

    suspend fun deleteAccount(header: Pair<String, String>, req: DeleteAccountRequest): Boolean
}