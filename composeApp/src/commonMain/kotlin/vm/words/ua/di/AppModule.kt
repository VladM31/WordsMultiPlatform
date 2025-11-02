package vm.words.ua.di

import androidx.compose.runtime.Composable
import org.kodein.di.DI
import vm.words.ua.auth.di.authModules
import vm.words.ua.core.di.coreModule
import vm.words.ua.exercise.di.exerciseModule
import vm.words.ua.learning.di.learningModule
import vm.words.ua.navigation.di.navigationModule
import vm.words.ua.playlist.di.playlistModule
import vm.words.ua.subscribes.di.subscribesModule
import vm.words.ua.words.di.managersWordsModule
import vm.words.ua.words.di.viewModelWordsModule


/**
 * Все модули приложения
 */
val appModules = DI {
    import(navigationModule)
    import(coreModule)
    import(authModules)
    import(subscribesModule)
    import(learningModule)
    import(exerciseModule)
    import(playlistModule)
    import(managersWordsModule)
    import(viewModelWordsModule)
}

object DiContainer {
    val di = appModules
}

/**
 * Composable делегат для получения зависимости из DI
 * Автоматически оборачивает вызов в remember для стабильности между рекомпозициями
 *
 * На Android использует androidx.lifecycle.viewmodel для ViewModel,
 * чтобы сохранять состояние при изменении конфигурации (поворот экрана)
 *
 * Использование:
 * ```
 * val viewModel = rememberInstance<LoginViewModel>()
 * ```
 */
@Composable
expect inline fun <reified T : Any> rememberInstance(): T
