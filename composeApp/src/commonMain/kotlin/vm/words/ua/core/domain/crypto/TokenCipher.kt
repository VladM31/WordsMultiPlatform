package vm.words.ua.core.domain.crypto

/**
 * Multiplatform interface for token encryption/decryption
 */
interface TokenCipher {
    fun encrypt(value: String): String
    fun decrypt(value: String): String
}

/**
 * Factory to create platform-specific TokenCipher implementation
 */
expect object TokenCipherFactory {
    fun create(): TokenCipher
}

