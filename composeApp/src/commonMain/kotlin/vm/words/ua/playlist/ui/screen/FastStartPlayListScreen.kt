package vm.words.ua.playlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.playlist.domain.models.enums.PlayListType
import vm.words.ua.playlist.ui.actions.FastStartPlayListAction
import vm.words.ua.playlist.ui.vms.FastStartPlayListVm


@Composable
fun FastStartPlayListScreen(
    navController: SimpleNavController,
) {
    val viewModel = rememberInstance<FastStartPlayListVm>()
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppToolBar(
            title = "Playlists",
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            showAdditionalButton = state.isLoading.not(),
            additionalButtonVector = Icons.Outlined.Search
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AppTheme.PrimaryColor
            )
            return@Box
        }

        BottomMenu(
            visibility = state.type,
            onClick = { newVisibility ->
                viewModel.send(FastStartPlayListAction.ChangeType(newVisibility))
            }
        )
    }


}

@Composable
fun BoxScope.BottomMenu(
    visibility: PlayListType,
    onClick: (PlayListType) -> Unit,
) {
    val iconSize = rememberIconSize() * 1.2f
    val iconModifier = Modifier.size(iconSize)
    val textSize = rememberFontSize() * 0.7
    val maxWidth = rememberInterfaceMaxWidth() * 1.15f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryBack)
            .align(Alignment.BottomCenter)
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .fillMaxWidth()
                .background(AppTheme.PrimaryBack),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Item(
                imageVector = Icons.AutoMirrored.Filled.Article,
                thisType = PlayListType.YOUR,
                currentType = visibility,
                text = "Your",
                onClick = onClick,
                iconModifier = iconModifier,
                textSize = textSize,
                isDisabled = true
            )

            Item(
                imageVector = Icons.Filled.Explore,
                thisType = PlayListType.PUBLIC,
                currentType = visibility,
                text = "Public",
                onClick = onClick,
                iconModifier = iconModifier,
                textSize = textSize
            )
        }
    }
}

@Composable
fun Item(
    imageVector: ImageVector,
    thisType: PlayListType,
    currentType: PlayListType,
    text: String,
    onClick: (PlayListType) -> Unit,
    iconModifier: Modifier = Modifier,
    isDisabled: Boolean = false,
    textSize: TextUnit
) {
    val isSelected = thisType == currentType
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            enabled = isSelected.not() && isDisabled.not(),
            onClick = { onClick(thisType) },
            modifier = iconModifier,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = text,
                modifier = Modifier.fillMaxSize(),
                tint = getColor(isSelected, isDisabled)
            )
        }
        Text(
            text = text,
            color = getColor(isSelected, isDisabled),
            fontSize = textSize,
            textAlign = TextAlign.Center
        )
    }
}

private fun getColor(isSelected: Boolean, isDisabled: Boolean): Color {
    if (isDisabled) {
        return AppTheme.PrimaryDisable
    }

    return if (isSelected) AppTheme.PrimaryColor else AppTheme.PrimaryColorDark
}
