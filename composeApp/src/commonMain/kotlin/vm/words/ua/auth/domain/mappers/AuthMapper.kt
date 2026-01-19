package vm.words.ua.auth.domain.mappers

import kotlinx.datetime.Clock
import vm.words.ua.auth.domain.models.SignUpModel
import vm.words.ua.auth.net.requests.SignUpRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.auth.net.responses.TelegramLoginRespond
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
        currency = currency, password = password
    )
}

fun TelegramLoginRespond.toUser(): User {
    if (user == null) {
        throw IllegalArgumentException("User is null")
    }

    return User(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        phoneNumber = user.phoneNumber,
        email = user.email,
        currency = user.currency,
        role = user.role,
        dateOfLonIn = Clock.System.now().toEpochMilliseconds()
    )
}