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
import vm.words.ua.core.utils.getScaleFactor

/**
 * Simple data holder for a grid button.
 */
data class GridButtonItem(
    val text: String,
    val onClick: () -> Unit = {}
)

@Composable
fun ButtonsGrid(
    items: List<GridButtonItem>,
    modifier: Modifier = Modifier,
    baseButtonWidth: Dp = 300.dp,
    baseTextSize: Float = 40f
) {
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        // Получаем коэффициент масштабирования на основе ширины экрана
        val scaleFactor = getScaleFactor(maxWidth)

        // Масштабируем все размеры
        val maxButtonWidth = baseButtonWidth * scaleFactor
        val gap = (10 * scaleFactor).dp
        val horizontalPadding = (12 * scaleFactor).dp
        val verticalPadding = (8 * scaleFactor).dp
        val minButtonHeight = (56 * scaleFactor).dp
        val verticalSpacing = (6 * scaleFactor).dp
        val bottomSpacing = (24 * scaleFactor).dp

        // Размер текста на основе масштаба
        val textSize = (baseTextSize * scaleFactor).sp

        // decide columns: up to 2
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
                        contentColor = AppTheme.PrimaryBack
                    )

                    for (item in row) {
                        Button(
                            onClick = item.onClick,
                            colors = buttonColors,
                            modifier = Modifier
                                .width(itemWidth)
                                .heightIn(min = minButtonHeight)
                        ) {
                            Text(text = item.text, fontSize = textSize)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(bottomSpacing))
        }
    }
}

