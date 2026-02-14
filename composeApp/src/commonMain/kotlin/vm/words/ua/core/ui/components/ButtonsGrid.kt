package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getScaleFactor

/**
 * Simple data holder for a grid button.
 */
data class GridButtonItem(
    val text: String,
    val modifier: Modifier = Modifier,
    val isAvailable: Boolean = true,
    val icon: ImageVector? = null,
    val onClick: () -> Unit = {}
)

@Composable
fun ButtonsGrid(
    items: List<GridButtonItem>,
    modifier: Modifier = Modifier,
    baseButtonWidth: Dp = 300.dp,
    baseTextSize: Float = 32f
) {
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        val scaleFactor = getScaleFactor(maxWidth)

        val maxButtonWidth = baseButtonWidth * scaleFactor
        val gap = (16 * scaleFactor).dp
        val horizontalPadding = (16 * scaleFactor).dp
        val verticalPadding = (12 * scaleFactor).dp
        val minButtonHeight = (64 * scaleFactor).dp
        val verticalSpacing = (10 * scaleFactor).dp
        val bottomSpacing = (24 * scaleFactor).dp
        val cornerRadius = (8 * scaleFactor).dp
        val shadowElevation = (4 * scaleFactor).dp

        val textSize = (baseTextSize * scaleFactor).sp

        val columns = if (maxWidth >= (maxButtonWidth * 2 + gap)) 2 else 1

        val availableForItems = maxWidth - gap * (columns - 1) - horizontalPadding * 2
        val rawItemWidth = availableForItems / columns
        val itemWidth = if (rawItemWidth >= maxButtonWidth) maxButtonWidth else rawItemWidth

        val rows = items.chunked(columns)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in rows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = verticalSpacing),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    val buttonColors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.PrimaryColor,
                        contentColor = AppTheme.PrimaryBack,
                        disabledContainerColor = AppTheme.PrimaryColor.copy(alpha = 0.4f),
                        disabledContentColor = AppTheme.PrimaryBack.copy(alpha = 0.5f)
                    )

                    for (item in row) {
                        Button(
                            onClick = item.onClick,
                            colors = buttonColors,
                            enabled = item.isAvailable,
                            shape = RoundedCornerShape(cornerRadius),
                            modifier = item.modifier
                                .width(itemWidth)
                                .heightIn(min = minButtonHeight)
                                .shadow(
                                    elevation = if (item.isAvailable) shadowElevation else 0.dp,
                                    shape = RoundedCornerShape(cornerRadius),
                                    clip = false
                                ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 6.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (item.icon != null) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.text,
                                        modifier = Modifier.size((textSize.value * 1.0f).dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                                Text(
                                    text = item.text,
                                    fontSize = textSize,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    maxLines = 2,
                                    softWrap = true,
                                    lineHeight = textSize * 1.3f
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(bottomSpacing))
        }
    }
}

