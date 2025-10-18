package vm.words.ua.auth.net.responses

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val success: Boolean = false,
    val error: String? = null
) {
    val isSuccess: Boolean
        get() = success && error == null

    companion object {
        fun error(message: String): SignUpResponse {
            return SignUpResponse(success = false, error = message)
        }

        fun success(): SignUpResponse {
            return SignUpResponse(success = true, error = null)
        }
    }
}

