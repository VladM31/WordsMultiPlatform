package vm.words.ua.auth.domain.factories

import vm.words.ua.auth.domain.managers.ContactRequestStatus
import vm.words.ua.auth.domain.managers.TelegramWebAppManager

private object EmptyTelegramWebAppManager : TelegramWebAppManager {
    override val initData: String = ""
    override val userJson: String = ""
    override suspend fun requestContact(): ContactRequestStatus = ContactRequestStatus.Unavailable
}

actual fun createTelegramWebAppManager(): TelegramWebAppManager = EmptyTelegramWebAppManager

