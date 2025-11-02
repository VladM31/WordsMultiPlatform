package vm.words.ua.core.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.navigation.SimpleNavController

@Composable
fun UpdateScreen(
    navController: SimpleNavController
) {
    val fontSize = getFontSize()
    val uriHandler = LocalUriHandler.current

    var currentVersion by remember { mutableStateOf(AppRemoteConfig.currentVersion) }
    var remoteVersion by remember { mutableStateOf(AppRemoteConfig.version) }
    var updateLink by remember { mutableStateOf(AppRemoteConfig.updateLink) }


    Column(modifier = Modifier.fillMaxSize()) {
        AppToolBar(
            title = "Update App",
            navController = navController,
            showBackButton = false
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Current version: $currentVersion",
                color = AppTheme.PrimaryColor,
                fontSize = fontSize,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Available version: $remoteVersion",
                color = AppTheme.PrimaryColor,
                fontSize = fontSize,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))


            Button(
                onClick = { uriHandler.openUri(updateLink.ifBlank { AppRemoteConfig.baseUrl }) }
            ) {
                Text(
                    text = "Update Now",
                    fontSize = fontSize,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        Spacer(Modifier.weight(1f))
    }
}

