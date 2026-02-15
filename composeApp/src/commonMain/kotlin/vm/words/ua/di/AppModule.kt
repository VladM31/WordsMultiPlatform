package vm.words.ua.di

import androidx.compose.runtime.Composable
import org.kodein.di.DI
import vm.words.ua.auth.di.authModules
import vm.words.ua.core.di.coreModule
import vm.words.ua.exercise.di.exerciseModule
import vm.words.ua.learning.di.learningModule
import vm.words.ua.navigation.di.navigationModule
import vm.words.ua.playlist.di.playlistModule
import vm.words.ua.settings.di.settingsModule
import vm.words.ua.subscribes.di.subscribesModule
import vm.words.ua.utils.net.di.newUtilsModule
import vm.words.ua.words.di.wordsModule

val appModules = DI {
    import(navigationModule)
    import(coreModule)
    import(newUtilsModule)
    import(authModules)
    import(subscribesModule)
    import(wordsModule)
    import(learningModule)
    import(exerciseModule)
    import(playlistModule)
    import(settingsModule)
}

object DiContainer {
    val di = appModules
}


@Composable
expect inline fun <reified T : Any> rememberInstance(): T
