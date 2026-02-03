package vm.words.ua.learning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberScaleFactor

@Composable
fun CounterRow(
    count: Int,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 0,
    maxValue: Int = Int.MAX_VALUE
) {
    val iconSize = 56.dp * rememberScaleFactor()
    val iconBtnSize = iconSize * 1.05f


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMinusClick,
            enabled = count > minValue,
            modifier = Modifier
                .size(iconBtnSize)
                .background(
                    color = AppTheme.PrimaryBackLight,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = "Minus",
                modifier = Modifier.size(iconSize),
                tint = if (count > minValue) AppTheme.PrimaryGreen
                else AppTheme.PrimaryDisable
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = count.toString(),
            color = AppTheme.PrimaryGreen,
            fontSize = rememberFontSize(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.widthIn(min = 50.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(24.dp))

        IconButton(
            onClick = onPlusClick,
            enabled = count < maxValue,
            modifier = Modifier
                .size(iconBtnSize)
                .background(
                    color = AppTheme.PrimaryBackLight,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Plus",
                modifier = Modifier.size(iconSize),
                tint = if (count < maxValue) AppTheme.PrimaryGreen
                else AppTheme.PrimaryDisable
            )
        }
    }
}