package vm.words.ua.utils.net.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize

@Composable
fun NoInternetBanner(
    modifier: Modifier = Modifier
) {
    val platform = currentPlatform()
    val fontSize = rememberFontSize()
    val iconSize = rememberIconSize() * 1.2f

    val instruction = when (platform) {
        AppPlatform.ANDROID,
        AppPlatform.IOS -> "Try turning Wi-Fi off and on or check mobile data"

        AppPlatform.JVM -> "Check your internet connection or restart your router"
        AppPlatform.JS,
        AppPlatform.WASM -> "Check your internet connection in the browser"

        AppPlatform.UNKNOWN -> "Check your internet connection"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AppTheme.ColorScheme.errorContainer,
        tonalElevation = 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.WifiOff,
                    contentDescription = "No internet",
                    tint = AppTheme.ColorScheme.onErrorContainer,
                    modifier = Modifier.size(iconSize)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No internet connection",
                        color = AppTheme.ColorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = fontSize
                    )

                    Text(
                        text = instruction,
                        color = AppTheme.ColorScheme.onErrorContainer.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = fontSize
                    )
                }
            }
        }
    }
}
