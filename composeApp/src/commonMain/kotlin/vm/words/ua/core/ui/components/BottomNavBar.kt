package vm.words.ua.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import wordsmultiplatform.composeapp.generated.resources.Res
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.navigation.Screen
import wordsmultiplatform.composeapp.generated.resources.home
import wordsmultiplatform.composeapp.generated.resources.home_no_active
import wordsmultiplatform.composeapp.generated.resources.play_list_nav
import wordsmultiplatform.composeapp.generated.resources.play_list_nav_no_active
import wordsmultiplatform.composeapp.generated.resources.setting
import wordsmultiplatform.composeapp.generated.resources.setting_no_active

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize = 80.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(iconSize + 32.dp)
            .background(AppTheme.PrimaryBack),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Playlist button
        val isPlaylist = currentRoute == Screen.PlayList.route
        val playlistRes = if (isPlaylist) Res.drawable.play_list_nav else Res.drawable.play_list_nav_no_active
        IconButton(
            enabled = isPlaylist.not(),
            onClick = { onNavigate(Screen.PlayList) },
            modifier = Modifier.size(iconSize)
        ) {
            Image(
                painter = painterResource(playlistRes),
                contentDescription = "Playlist",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Home button
        val isHome = currentRoute == Screen.Home.route
        val homeRes = if (isHome) Res.drawable.home else Res.drawable.home_no_active
        IconButton(
            enabled = isHome.not(),
            onClick = { onNavigate(Screen.Home) },
            modifier = Modifier.size(iconSize)
        ) {
            Image(
                painter = painterResource(homeRes),
                contentDescription = "Home",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        val isSettings = currentRoute == Screen.Settings.route
        val settingRes = if (isSettings) Res.drawable.setting else Res.drawable.setting_no_active
        IconButton(
            enabled = isSettings.not(),
            onClick = { onNavigate(Screen.Settings) },
            modifier = Modifier.size(iconSize)
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
