package vm.words.ua.core.firebase

/**
 * Заглушка для WasmJS платформы (Firebase не поддерживается)
 * Возвращает дефолтные значения
 */
actual object AppRemoteConfig {
    actual val instructionLastUpdate: String
        get() = "2025-10-06T22:35:29.604559800+03:00"
    
    actual val instructionLink: String
        get() = "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/instruction.txt"
    
    actual val localHost: String
        get() = "vps2498837.fastwebserver.de"
    
    actual val policyLastUpdateAt: String
        get() = "2025-10-12T01:52:29.604559800+03:00"
    
    actual val policyLink: String
        get() = "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/policy.txt"
    
    actual val telegramBotLink: String
        get() = "https://t.me/needlework_number_bot"
    
    actual val updateLink: String
        get() = "https://github.com/VladM31/AndroidWordApplication/releases/download/Card_Payment/Words-dev-v2.0.1.apk"
    
    actual val version: String
        get() = "2.0.1"
    
    actual suspend fun initialize() {
        println("Firebase Remote Config не поддерживается на WasmJS платформе. Используются дефолтные значения.")
    }
    
    actual suspend fun refresh() {
        println("Firebase Remote Config не поддерживается на WasmJS платформе. Используются дефолтные значения.")
    }
}
