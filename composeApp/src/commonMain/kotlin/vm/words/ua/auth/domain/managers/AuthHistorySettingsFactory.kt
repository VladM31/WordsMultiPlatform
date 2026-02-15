package vm.words.ua.auth.domain.managers

import com.russhwolf.settings.Settings


expect object AuthHistorySettingsFactory {
    fun create(): Settings
}

