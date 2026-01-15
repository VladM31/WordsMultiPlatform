package vm.words.ua.words.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
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
    visible: Boolean,
    onUnselect: () -> Unit,
    onApply: () -> Unit,
    showDelete: Boolean = false,
    deleteLabel: String = "Delete",
    onDelete: () -> Unit = {},
    applyLabel: String = "Apply",
    modifier: Modifier = Modifier
) {
    if (!visible) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryBack)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onUnselect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryBack,
                    contentColor = AppTheme.PrimaryGreen
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(text = "Clear", fontSize = rememberLabelFontSize())
            }

            if (showDelete) {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.Error,
                        contentColor = AppTheme.PrimaryBack
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(text = deleteLabel, fontSize = rememberLabelFontSize())
                }
            }

            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.PrimaryGreen,
                    contentColor = AppTheme.PrimaryBack
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(text = applyLabel, fontSize = rememberLabelFontSize())
            }
        }
    }
}
