package vm.words.ua.auth.domain.models

enum class OwnershipProvider(val value: String) {
    GOOGLE("GOOGLE"),
    TELEGRAM("TELEGRAM")
}

/**
 * Доказательство владения аккаунтом. Telegram-код одноразовый и
 * должен быть подтверждён кнопкой в боте до использования.
 */
data class OwnershipProof(
    val provider: OwnershipProvider,
    val idToken: String? = null,
    val code: String? = null,
    /** `initData` Telegram Mini App: подпись проверяет бот, кнопка в боте не нужна */
    val initData: String? = null
) {
    companion object {
        fun google(idToken: String) = OwnershipProof(OwnershipProvider.GOOGLE, idToken = idToken)
        fun telegram(code: String) = OwnershipProof(OwnershipProvider.TELEGRAM, code = code)
        fun miniApp(initData: String) = OwnershipProof(OwnershipProvider.TELEGRAM, initData = initData)
    }
}
