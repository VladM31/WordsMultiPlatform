package vm.words.ua.core.firebase

/**
 * Константы ключей для Firebase Remote Config
 */
object RemoteConfigKeys {
    const val INSTRUCTION_LAST_UPDATE = "instruction_last_update"
    const val INSTRUCTION_LINK = "instruction_link"
    const val LOCAL_HOST = "local_host"
    const val POLICY_LAST_UPDATE_AT = "policy_last_update_at"
    const val POLICY_LINK = "policy_link"
    const val TELEGRAM_BOT_LINK = "telegram_bot_link"
    const val UPDATE_LINK = "update_link"
    const val VERSION = "version"
}

/**
 * Дефолтные значения для Remote Config
 */
object RemoteConfigDefaults {
    val defaults = mapOf(
        RemoteConfigKeys.INSTRUCTION_LAST_UPDATE to "2025-10-06T22:35:29.604559800+03:00",
        RemoteConfigKeys.INSTRUCTION_LINK to "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/instruction.txt",
        RemoteConfigKeys.LOCAL_HOST to "vps2498837.fastwebserver.de",
        RemoteConfigKeys.POLICY_LAST_UPDATE_AT to "2025-10-12T01:52:29.604559800+03:00",
        RemoteConfigKeys.POLICY_LINK to "https://raw.githubusercontent.com/VladM31/AndroidWordApplication/refs/heads/master/documents/policy.txt",
        RemoteConfigKeys.TELEGRAM_BOT_LINK to "https://t.me/needlework_number_bot",
        RemoteConfigKeys.UPDATE_LINK to "https://github.com/VladM31/AndroidWordApplication/releases/download/Card_Payment/Words-dev-v2.0.1.apk",
        RemoteConfigKeys.VERSION to "2.0.1"
    )
}
