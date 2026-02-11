package vm.words.ua

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import vm.words.ua.core.ui.components.AuthWatcher
import vm.words.ua.core.ui.screen.LoaderScreen
import vm.words.ua.core.ui.theme.rememberCurrentTheme
import vm.words.ua.core.utils.AppWindowProvider
import vm.words.ua.navigation.AppNavGraph


@Composable
@Preview
fun App(modifier: Modifier = Modifier) = AppWindowProvider {
    // Observe theme changes to trigger recomposition
    val currentTheme by rememberCurrentTheme()

    MaterialTheme(
        colorScheme = currentTheme.colorScheme
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = currentTheme.primaryBack
        ) {
            var isInitialized by rememberSaveable { mutableStateOf(false) }
            if (isInitialized.not()) {
                LoaderScreen {
                    isInitialized = true
                }
                return@Surface
            }
            AppNavGraph()
            AuthWatcher()
        }
    }
}
