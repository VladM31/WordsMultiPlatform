package vm.words.ua.core.utils

// commonMain

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*

@Stable
data class AppWindowDp(val width: Dp, val height: Dp)

val LocalAppWindowDp = staticCompositionLocalOf { AppWindowDp(0.dp, 0.dp) }

@Composable
fun AppWindowProvider(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize().then(modifier)) {
        val size = remember(maxWidth, maxHeight) { AppWindowDp(maxWidth, maxHeight) }
        CompositionLocalProvider(LocalAppWindowDp provides size) {
            content()
        }
    }
}

/** Утилиты для чтения значения в dp из любого места. */
@Composable
fun appWidthDp(): Dp = LocalAppWindowDp.current.width

@Composable
fun appHeightDp(): Dp = LocalAppWindowDp.current.height

@Composable
fun appSizeDp(): DpSize =
    DpSize(LocalAppWindowDp.current.width, LocalAppWindowDp.current.height)
