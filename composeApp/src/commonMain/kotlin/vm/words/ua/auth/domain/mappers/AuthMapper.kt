package vm.words.ua.auth.domain.mappers

import kotlinx.datetime.Clock
import vm.words.ua.auth.domain.models.data.SignUpModel
import vm.words.ua.auth.net.requests.SignUpRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.core.domain.models.User

internal fun AuthResponse.User.toUser(): User {
    return User(
        id = id, firstName = firstName, lastName = lastName, phoneNumber = phoneNumber,
        email = email, currency = currency, role = role, dateOfLonIn = Clock.System.now().toEpochMilliseconds()
    )
}

fun SignUpModel.toRequest(): SignUpRequest {
    return SignUpRequest(
        firstName = firstName, lastName = lastName, phoneNumber = phoneNumber,
        email = email, currency = currency, password = password
    )
}
