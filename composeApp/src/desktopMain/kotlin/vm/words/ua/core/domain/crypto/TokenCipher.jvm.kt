package vm.words.ua.core.domain.crypto

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class JvmTokenCipher : TokenCipher {
    private val key = "WordsApp12345678" // 16 bytes for AES-128
    private val secretKey = SecretKeySpec(key.toByteArray(), "AES")

    override fun encrypt(value: String): String {
        if (value.isEmpty()) return value

        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encrypted = cipher.doFinal(value.toByteArray())
            Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            value // fallback to plain text on error
        }
    }

    override fun decrypt(value: String): String {
        if (value.isEmpty()) return value

        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decoded = Base64.getDecoder().decode(value)
            val decrypted = cipher.doFinal(decoded)
            String(decrypted)
        } catch (e: Exception) {
            value // fallback to returning as-is on error
        }
    }
}

actual object TokenCipherFactory {
    actual fun create(): TokenCipher = JvmTokenCipher()
}

