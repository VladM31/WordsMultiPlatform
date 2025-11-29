package vm.words.ua.utils.storage.crypto

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface CryptoEngine {
    fun encrypt(plain: String): String
    fun decrypt(cipher: String): String
}

class XorCryptoEngine(
    private val key: String,
) : CryptoEngine {

    private fun xorBytes(data: ByteArray, keyBytes: ByteArray): ByteArray {
        val out = ByteArray(data.size)
        for (i in data.indices) {
            out[i] = (data[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
        }
        return out
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun encrypt(plain: String): String {
        val data = plain.encodeToByteArray()
        val keyBytes = key.encodeToByteArray()
        val xored = xorBytes(data, keyBytes)
        return Base64.encode(xored)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decrypt(cipher: String): String {
        val keyBytes = key.encodeToByteArray()
        val decoded = Base64.decode(cipher)
        val xored = xorBytes(decoded, keyBytes)
        return xored.decodeToString()
    }
}