package vm.words.ua.core.firebase

/**
 * Ожидаемая (expect) реализация Remote Config для разных платформ
 */
expect object AppRemoteConfig {
    val instructionLastUpdate: String
    val instructionLink: String
    val localHost: String
    val policyLastUpdateAt: String
    val policyLink: String
    val telegramBotLink: String
    val updateLink: String
    val version: String

    suspend fun initialize()
    suspend fun refresh()
}
