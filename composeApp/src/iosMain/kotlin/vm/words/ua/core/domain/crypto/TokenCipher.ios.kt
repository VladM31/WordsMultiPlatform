package vm.words.ua.core.domain.crypto

import platform.Foundation.*

class IosTokenCipher : TokenCipher {
    override fun encrypt(value: String): String {
        // Simple base64 as placeholder; replace with CryptoKit if needed
        if (value.isEmpty()) return value
        val data = (value as NSString).dataUsingEncoding(NSUTF8StringEncoding) ?: return value
        return data.base64EncodedStringWithOptions(0u)
    }

    override fun decrypt(value: String): String {
        if (value.isEmpty()) return value
        val data = NSData.create(base64EncodedString = value, options = 0u) ?: return value
        val str = NSString.create(data, NSUTF8StringEncoding) ?: return value
        return str as String
    }
}

actual object TokenCipherFactory {
    actual fun create(): TokenCipher = IosTokenCipher()
}

