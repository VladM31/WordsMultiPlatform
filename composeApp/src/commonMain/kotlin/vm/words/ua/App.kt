package vm.words.ua

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.navigation.AppNavGraph
import vm.words.ua.navigation.rememberSimpleNavController

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = AppTheme.ColorSchema
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.PrimaryBack
        ) {
            val navController = rememberSimpleNavController()
            AppNavGraph(navController = navController)
        }
    }
}