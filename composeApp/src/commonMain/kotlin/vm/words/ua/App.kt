package vm.words.ua

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AuthWatcher
import vm.words.ua.core.ui.screen.LoaderScreen
import vm.words.ua.core.utils.AppWindowProvider
import vm.words.ua.navigation.AppNavGraph
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable


@Composable
@Preview
fun App() = AppWindowProvider {


    MaterialTheme(
        colorScheme = AppTheme.ColorSchema
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.PrimaryBack
        ) {
            var isInitialized by rememberSaveable { mutableStateOf(false) }
            // Show LoaderScreen until initialization is complete
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
