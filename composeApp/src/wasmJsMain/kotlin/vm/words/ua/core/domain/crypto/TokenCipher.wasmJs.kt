package vm.words.ua.core.domain.crypto

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * WASM implementation using base64 encoding
 * TODO: Implement proper AES encryption when WASM crypto libraries are available
 */
class WasmTokenCipher : TokenCipher {

    @OptIn(ExperimentalEncodingApi::class)
    override fun encrypt(value: String): String {
        if (value.isEmpty()) return value

        return try {
            Base64.encode(value.encodeToByteArray())
        } catch (e: Exception) {
//            console.error("Encryption error:", e)
            value
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decrypt(value: String): String {
        if (value.isEmpty()) return value

        return try {
            Base64.decode(value).decodeToString()
        } catch (e: Exception) {
//            console.error("Decryption error:", e)
            value
        }
    }
}

actual object TokenCipherFactory {
    actual fun create(): TokenCipher = WasmTokenCipher()
}

