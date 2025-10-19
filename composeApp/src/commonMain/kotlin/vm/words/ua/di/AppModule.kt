package vm.words.ua.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import vm.words.ua.auth.di.authModules
import vm.words.ua.core.di.coreModule
import vm.words.ua.playlist.di.playlistModule


/**
 * Все модули приложения
 */
val appModules = DI {
    import(coreModule)
    import(authModules)
    import(playlistModule)
}

object DiContainer {
    val di = appModules
}

/**
 * Composable делегат для получения зависимости из DI
 * Автоматически оборачивает вызов в remember для стабильности между рекомпозициями
 *
 * Использование:
 * ```
 * val viewModel = rememberInstance<LoginViewModel>()
 * ```
 */
@Composable
inline fun <reified T : Any> rememberInstance(): T {
    return remember {
        DiContainer.di.direct.instance<T>()
    }
}
