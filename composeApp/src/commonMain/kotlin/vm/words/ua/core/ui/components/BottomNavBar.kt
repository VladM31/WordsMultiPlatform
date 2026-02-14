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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
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
    val maxWidth = rememberInterfaceMaxWidth() * 1.2f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryBack)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Playlist button
        Item(
            imageVector = Icons.AutoMirrored.Filled.Article,
            thisScreen = Screen.PlayList,
            currentScreen = currentRoute,
            text = "Playlists",
            onNavigate = onNavigate,
            columnModifier = playListHintStep?.let { Modifier.viewHint(it, currentHintStep) } ?: Modifier,
            iconModifier = iconModifier,
            textSize = textSize
        )



        // Home button
        Item(
            imageVector = Icons.Filled.Home,
            thisScreen = Screen.Home,
            currentScreen = currentRoute,
            text = "Home",
            onNavigate = onNavigate,
            columnModifier = homeHintStep?.let { Modifier.viewHint(it, currentHintStep) } ?: Modifier,
            iconModifier = iconModifier,
            textSize = textSize
        )

        // Settings button
        Item(
            imageVector = Icons.Default.Settings,
            thisScreen = Screen.Settings,
            currentScreen = currentRoute,
            text = "Settings",
            onNavigate = onNavigate,
            columnModifier = settingHintStep?.let { Modifier.viewHint(it, currentHintStep) } ?: Modifier,
            iconModifier = iconModifier,
            textSize = textSize
        )
    }
}

@Composable
fun Item(
    imageVector: ImageVector,
    thisScreen: Screen,
    currentScreen: Screen,
    text: String,
    onNavigate: (Screen) -> Unit,
    columnModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    textSize: TextUnit
) {
    val isSelected = thisScreen == currentScreen
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier
    ) {
        IconButton(
            enabled = isSelected.not(),
            onClick = { onNavigate(thisScreen) },
            modifier = iconModifier
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = text,
                modifier = Modifier.fillMaxSize(),
                tint = if (isSelected) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
            )
        }
        Text(
            text = text,
            color = if (isSelected) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark,
            fontSize = textSize,
            textAlign = TextAlign.Center
        )
    }
}
