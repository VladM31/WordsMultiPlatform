package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getScaleFactor

@Composable
fun WordInputPanel(
    text: String,
    onTextChange: (String) -> Unit,
    enabled: Boolean,
    onAddLetter: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    val scale = getScaleFactor()
    val width: Dp = remember(scale) {
        300.dp * scale
    }
    val icon: Dp = remember(scale) {
        50.dp * scale
    }
    val fontSize: TextUnit = getFontSize()


    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.width(width),
            color = AppTheme.PrimaryBack,
            border = BorderStroke(1.dp, AppTheme.PrimaryColor),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
            shadowElevation = 2.dp
        ) {
            Box(Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = onTextChange,
                    enabled = enabled,
                    singleLine = true,
                    textStyle = TextStyle(color = AppTheme.PrimaryColor, fontSize = fontSize),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        cursorColor = AppTheme.PrimaryColor,
                        disabledTextColor = AppTheme.PrimaryColor.copy(alpha = 0.6f)
                    )
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.width(width),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAddLetter, modifier = Modifier.size(icon)) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Add letter", tint = AppTheme.PrimaryColor)
            }
        }
    }
}

