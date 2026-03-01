package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberLabelFontSize

@Composable
fun TextInput(
    value: String?,
    label: String,
    maxLength: Int = 255,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    color: Color = AppTheme.PrimaryColor,
    modifier: Modifier = Modifier,
    onValueChange: (String?) -> Unit
) {
    val fontSize = rememberFontSize()
    val labelFontSize = rememberLabelFontSize()
    val currentModifier = if (modifier == Modifier) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.fillMaxWidth().then(modifier)
    }

    OutlinedTextField(
        value = value.orEmpty(),
        onValueChange = {
            val truncated = it.take(maxLength)
            onValueChange(truncated.ifBlank { null })
        },
        textStyle = TextStyle(fontSize = fontSize),
        label = { Text(label, color = color, fontSize = labelFontSize) },
        modifier = currentModifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color,
            unfocusedBorderColor = color.copy(alpha = 0.5f),
            focusedTextColor = color,
            unfocusedTextColor = color,
            cursorColor = color
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled
    )
}