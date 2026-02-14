package vm.words.ua.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.rememberIconSize

/**
 * Simple data holder for a grid button.
 */
data class GridButtonItem(
    val text: String,
    val modifier: Modifier = Modifier,
    val isAvailable: Boolean = true,
    val icon: ImageVector? = null,
    val iconPainter: Painter? = null,
    val onClick: () -> Unit = {}
)

@Composable
fun ButtonsGrid(
    items: List<GridButtonItem>,
    modifier: Modifier = Modifier,
    baseButtonWidth: Dp = 300.dp,
    baseTextSize: Float = 30f
) {
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        val scaleFactor = getScaleFactor(maxWidth)

        val maxButtonWidth = baseButtonWidth * scaleFactor
        val gap = (4 * scaleFactor).dp
        val horizontalPadding = (8 * scaleFactor).dp
        val verticalPadding = (8 * scaleFactor).dp
        val minButtonHeight = (64 * scaleFactor).dp
        val verticalSpacing = (6 * scaleFactor).dp
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
                RowOfButtons(
                    verticalSpacing,
                    gap,
                    row,
                    cornerRadius,
                    itemWidth,
                    minButtonHeight,
                    shadowElevation,
                    textSize
                )
            }
            Spacer(modifier = Modifier.height(bottomSpacing))
        }
    }
}

@Composable
private fun RowOfButtons(
    verticalSpacing: Dp,
    gap: Dp,
    row: List<GridButtonItem>,
    cornerRadius: Dp,
    itemWidth: Dp,
    minButtonHeight: Dp,
    shadowElevation: Dp,
    textSize: TextUnit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalSpacing),
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))

        val buttonColors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.PrimaryBack,
            contentColor = AppTheme.PrimaryColor,
            disabledContainerColor = AppTheme.PrimaryBack.copy(alpha = 0.4f),
            disabledContentColor = AppTheme.PrimaryColor.copy(alpha = 0.5f)
        )

        for (item in row) {
            ButtonFromItem(item, buttonColors, cornerRadius, itemWidth, minButtonHeight, shadowElevation, textSize)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ButtonFromItem(
    item: GridButtonItem,
    buttonColors: ButtonColors,
    cornerRadius: Dp,
    itemWidth: Dp,
    minButtonHeight: Dp,
    shadowElevation: Dp,
    textSize: TextUnit
) {
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
            )
            .border(BorderStroke(2.dp, AppTheme.PrimaryColor), shape = RoundedCornerShape(cornerRadius)),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp,
            disabledElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.text,
                    modifier = Modifier.size(rememberIconSize() * 0.8f)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            if (item.iconPainter != null) {
                Icon(
                    painter = item.iconPainter,
                    contentDescription = item.text,
                    modifier = Modifier.size(rememberIconSize() * 0.8f)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = item.text,
                fontSize = textSize,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                softWrap = true,
                lineHeight = textSize * 1.15f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
