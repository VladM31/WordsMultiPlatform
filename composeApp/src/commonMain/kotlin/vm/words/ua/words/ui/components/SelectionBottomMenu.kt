package vm.words.ua.words.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberIconSize
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.core.utils.rememberLabelFontSize

/**
 * Bottom action menu shown when items are selected.
 *
 * @param visible show/hide the menu
 * @param onUnselect called when Unselect is pressed
 * @param onApply called when Apply is pressed
 */
@Composable
fun SelectionBottomMenu(

    onUnselect: () -> Unit,
    applyLabel: String = "Apply",
    onApply: () -> Unit,

    enableShowBtn: Boolean = false,
    onShow: () -> Unit = {},

    modifier: Modifier = Modifier,

    ) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryBack)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = rememberInterfaceMaxWidth())
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onUnselect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryBack,
                    contentColor = AppTheme.PrimaryColor
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 1.dp,
                    hoveredElevation = 4.dp
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(text = "Dismiss", fontSize = rememberLabelFontSize())
            }

            if (enableShowBtn) {
                Surface(
                    onClick = onShow,
                    modifier = Modifier.size(rememberIconSize() * 0.8f),
                    shape = RoundedCornerShape(12.dp),
                    color = AppColors.primaryColor.copy(alpha = 0.15f),
                    contentColor = AppColors.primaryColor
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fullscreen,
                        contentDescription = "Open word",
                        modifier = Modifier.size(rememberIconSize() * 0.5f)
                    )

                }
            }

            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryColor,
                    contentColor = AppTheme.PrimaryBack
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(text = applyLabel, fontSize = rememberLabelFontSize())
            }
        }
    }
}
