package vm.words.ua.auth.net.clients

import vm.words.ua.auth.net.requests.DeleteAccountRequest
import vm.words.ua.auth.net.requests.EditUserRequest
import vm.words.ua.auth.net.requests.SetPasswordRequest
import vm.words.ua.auth.net.requests.google.GoogleSignInRequest
import vm.words.ua.auth.net.responses.AuthResponse

/**
 * Методы `/user*`. Все требуют Bearer-токен (передаётся в [header]).
 * Ошибки приходят как [vm.words.ua.core.net.exceptions.ApiException] с текстом бэкенда.
 */
interface UserClient {

    /** `PUT /user`: имя и фамилия. В ответе новый токен и обновлённый пользователь. */
    suspend fun editUser(header: Pair<String, String>, req: EditUserRequest): AuthResponse

    /** `POST /user/password`: установка или смена пароля. */
    suspend fun setPassword(header: Pair<String, String>, req: SetPasswordRequest)

    /** `POST /user/identities/google`: привязка Google к текущему аккаунту. */
    suspend fun linkGoogle(header: Pair<String, String>, req: GoogleSignInRequest): AuthResponse.User

    /** `POST /user/delete`: удаление аккаунта. */
    suspend fun deleteAccount(header: Pair<String, String>, req: DeleteAccountRequest)
}
