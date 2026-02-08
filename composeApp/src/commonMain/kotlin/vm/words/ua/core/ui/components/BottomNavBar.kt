package vm.words.ua.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
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
    val iconSize = rememberIconSize() * 1.7f
    val iconModifier = Modifier.size(iconSize)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(iconSize + 4.dp)
            .background(AppTheme.PrimaryBack),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Playlist button
        val isPlaylist = currentRoute == Screen.PlayList
        IconButton(
            enabled = isPlaylist.not(),
            onClick = { onNavigate(Screen.PlayList) },
            modifier = playListHintStep?.let { iconModifier.viewHint(it, currentHintStep) } ?: iconModifier
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Article,
                contentDescription = "Playlist",
                modifier = Modifier.fillMaxSize(),
                tint = if (isPlaylist) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
            )
        }

        // Home button
        val isHome = currentRoute == Screen.Home
        IconButton(
            enabled = isHome.not(),
            onClick = { onNavigate(Screen.Home) },
            modifier = homeHintStep?.let { iconModifier.viewHint(it, currentHintStep) } ?: iconModifier
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home",
                modifier = Modifier.fillMaxSize(),
                tint = if (isHome) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
            )
        }

        val isSettings = currentRoute == Screen.Settings
        IconButton(
            enabled = isSettings.not(),
            onClick = { onNavigate(Screen.Settings) },
            modifier = settingHintStep?.let { iconModifier.viewHint(it, currentHintStep) } ?: iconModifier
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.fillMaxSize(),
                tint = if (isSettings) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
            )

        }
    }
}
