package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import androidx.compose.foundation.layout.BoxWithConstraints

/**
 * Simple data holder for a grid button.
 */
data class GridButtonItem(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun ButtonsGrid(
    items: List<GridButtonItem>,
    modifier: Modifier = Modifier,
    maxButtonWidth: Dp = 300.dp,
    textSizeSp: Float = 40f
) {
    val scrollState = rememberScrollState()
    val gap = 10.dp

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // decide columns: up to 2
        val columns = if (maxWidth >= (maxButtonWidth * 2 + gap)) 2 else 1

        val availableForItems = maxWidth - gap * (columns - 1)
        val rawItemWidth = availableForItems / columns
        val itemWidth = if (rawItemWidth >= maxButtonWidth) maxButtonWidth else rawItemWidth

        val rows = items.chunked(columns)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in rows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    val buttonColors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.PrimaryColor,
                        contentColor = AppTheme.PrimaryBack
                    )

                    for (item in row) {
                        Button(
                            onClick = item.onClick,
                            colors = buttonColors,
                            modifier = Modifier
                                .width(itemWidth)
                                .heightIn(min = 56.dp)
                        ) {
                            Text(text = item.text, fontSize = (textSizeSp).sp)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

