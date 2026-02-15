package vm.words.ua.core.domain.crypto


interface TokenCipher {
    fun encrypt(value: String): String
    fun decrypt(value: String): String
}


expect object TokenCipherFactory {
    fun create(): TokenCipher
}

