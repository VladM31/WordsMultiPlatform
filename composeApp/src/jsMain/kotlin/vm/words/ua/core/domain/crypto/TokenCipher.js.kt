package vm.words.ua.core.domain.crypto

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * JS implementation using Web Crypto API (simplified with base64 for now)
 * TODO: Implement proper AES encryption using Web Crypto API
 */
class JsTokenCipher : TokenCipher {

    @OptIn(ExperimentalEncodingApi::class)
    override fun encrypt(value: String): String {
        if (value.isEmpty()) return value

        return try {
            // Simple base64 encoding as placeholder
            // In production, use Web Crypto API: window.crypto.subtle.encrypt()
            Base64.encode(value.encodeToByteArray())
        } catch (e: Exception) {
            console.error("Encryption error:", e)
            value
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decrypt(value: String): String {
        if (value.isEmpty()) return value

        return try {
            // Simple base64 decoding as placeholder
            Base64.decode(value).decodeToString()
        } catch (e: Exception) {
            console.error("Decryption error:", e)
            value
        }
    }
}

actual object TokenCipherFactory {
    actual fun create(): TokenCipher = JsTokenCipher()
}

