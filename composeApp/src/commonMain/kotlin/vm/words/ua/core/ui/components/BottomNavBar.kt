package vm.words.ua.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.hints.ui.utils.ViewHintStep
import vm.words.ua.hints.ui.utils.viewHint
import vm.words.ua.navigation.Screen
import wordsmultiplatform.composeapp.generated.resources.*

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
    val iconSize = 80.dp
    val iconModifier = Modifier.size(iconSize)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(iconSize + 32.dp)
            .background(AppTheme.PrimaryBack),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Playlist button
        val isPlaylist = currentRoute == Screen.PlayList
        val playlistRes = if (isPlaylist) Res.drawable.play_list_nav else Res.drawable.play_list_nav_no_active
        IconButton(
            enabled = isPlaylist.not(),
            onClick = { onNavigate(Screen.PlayList) },
            modifier = playListHintStep?.let { iconModifier.viewHint(it, currentHintStep) } ?: iconModifier
        ) {
            Image(
                painter = painterResource(playlistRes),
                contentDescription = "Playlist",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Home button
        val isHome = currentRoute == Screen.Home
        val homeRes = if (isHome) Res.drawable.home else Res.drawable.home_no_active
        IconButton(
            enabled = isHome.not(),
            onClick = { onNavigate(Screen.Home) },
            modifier = homeHintStep?.let { iconModifier.viewHint(it, currentHintStep) } ?: iconModifier
        ) {
            Image(
                painter = painterResource(homeRes),
                contentDescription = "Home",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        val isSettings = currentRoute == Screen.Settings
        val settingRes = if (isSettings) Res.drawable.setting else Res.drawable.setting_no_active
        IconButton(
            enabled = isSettings.not(),
            onClick = { onNavigate(Screen.Settings) },
            modifier = settingHintStep?.let { iconModifier.viewHint(it, currentHintStep) } ?: iconModifier
        ) {
            Image(
                painter = painterResource(settingRes),
                contentDescription = "Settings",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}
