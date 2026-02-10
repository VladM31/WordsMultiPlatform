package vm.words.ua.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import vm.words.ua.navigation.Screen
import vm.words.ua.utils.hints.ui.utils.ViewHintStep
import vm.words.ua.utils.hints.ui.utils.viewHint

@Composable
fun BottomNavBar(
    currentRoute: Screen,
    modifier: Modifier = Modifier,
    playListHintStep: ViewHintStep? = null,
    homeHintStep: ViewHintStep? = null,
    settingHintStep: ViewHintStep? = null,
    currentHintStep: ViewHintStep? = null,
    onNavigate: (Screen) -> Unit,
) {
    val iconSize = rememberIconSize() * 1.2f
    val iconModifier = Modifier.size(iconSize)
    val textSize = rememberFontSize() * 0.7

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryBack)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Playlist button
        val isPlaylist = currentRoute == Screen.PlayList
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = playListHintStep?.let { Modifier.viewHint(it, currentHintStep) } ?: Modifier
        ) {
            IconButton(
                enabled = isPlaylist.not(),
                onClick = { onNavigate(Screen.PlayList) },
                modifier = iconModifier
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Article,
                    contentDescription = "Playlists",
                    modifier = Modifier.fillMaxSize(),
                    tint = if (isPlaylist) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
                )
            }
            Text(
                text = "Playlists",
                color = if (isPlaylist) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark,
                fontSize = textSize,
                textAlign = TextAlign.Center
            )
        }

        // Home button
        val isHome = currentRoute == Screen.Home
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = homeHintStep?.let { Modifier.viewHint(it, currentHintStep) } ?: Modifier
        ) {
            IconButton(
                enabled = isHome.not(),
                onClick = { onNavigate(Screen.Home) },
                modifier = iconModifier
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    modifier = Modifier.fillMaxSize(),
                    tint = if (isHome) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
                )
            }
            Text(
                text = "Home",
                color = if (isHome) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark,
                fontSize = textSize,
                textAlign = TextAlign.Center
            )
        }

        // Settings button
        val isSettings = currentRoute == Screen.Settings
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = settingHintStep?.let { Modifier.viewHint(it, currentHintStep) } ?: Modifier
        ) {
            IconButton(
                enabled = isSettings.not(),
                onClick = { onNavigate(Screen.Settings) },
                modifier = iconModifier
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.fillMaxSize(),
                    tint = if (isSettings) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
                )
            }
            Text(
                text = "Settings",
                color = if (isSettings) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark,
                fontSize = textSize,
                textAlign = TextAlign.Center
            )

        }
    }
}
