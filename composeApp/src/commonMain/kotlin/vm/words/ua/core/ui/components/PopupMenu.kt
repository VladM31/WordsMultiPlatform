package vm.words.ua.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberScaleFactor
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.setting

data class PopupMenuItem(
    val text: String,
    val icon: Painter? = null,
    val onClick: () -> Unit
)

@Composable
fun PopupMenuButton(
    items: List<PopupMenuItem>,
    modifier: Modifier = Modifier,
    buttonIcon: Painter = painterResource(Res.drawable.setting)
) {
    var expanded by remember { mutableStateOf(false) }

    val scaleFactor = rememberScaleFactor()

    val iconSize = (40 * scaleFactor).dp
    val buttonSize = iconSize * 1.2f
    val scaledTextSize = (16 * scaleFactor).sp
    val scaledMenuIconSize = (24 * scaleFactor).dp

    Box(modifier) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(buttonSize)
        ) {
            Icon(
                painter = buttonIcon,
                contentDescription = "Menu",
                tint = AppTheme.PrimaryColor,
                modifier = Modifier.size(iconSize)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, (8 * scaleFactor).dp),
            modifier = Modifier
                .background(AppTheme.SecondaryBack)
                .border(BorderStroke(2.dp, AppTheme.PrimaryColor))
        ) {
            items.forEachIndexed { index, item ->
                ShowDropdownMenuItem(
                    item = item,
                    scaledMenuIconSize = scaledMenuIconSize,
                    scaledTextSize = scaledTextSize,
                    scaleFactor = scaleFactor,
                    onDismiss = { expanded = false }
                )

                if (index < items.lastIndex) {
                    HorizontalDivider(
                        color = AppTheme.PrimaryGray.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}



@Composable
fun ShowDropdownMenuItem(
    item: PopupMenuItem,
    scaledMenuIconSize: Dp,
    scaledTextSize: androidx.compose.ui.unit.TextUnit,
    scaleFactor: Float,
    onDismiss: () -> Unit
){
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((12 * scaleFactor).dp)
            ) {
                item.icon?.let { icon ->
                    Icon(
                        painter = icon,
                        contentDescription = item.text,
                        tint = AppTheme.PrimaryColor,
                        modifier = Modifier.size(scaledMenuIconSize)
                    )
                }
                Text(
                    text = item.text,
                    color = AppTheme.PrimaryColor,
                    fontSize = scaledTextSize
                )
            }
        },
        onClick = {
            onDismiss()
            item.onClick()
        },
        colors = MenuDefaults.itemColors(
            textColor = AppTheme.PrimaryColor
        ),
        modifier = Modifier.background(AppTheme.SecondaryBack)
    )
}