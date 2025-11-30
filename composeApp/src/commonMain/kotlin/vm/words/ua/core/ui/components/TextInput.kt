package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getLabelFontSize
import vm.words.ua.core.utils.rememberFontSize

@Composable
fun TextInput(
    value: String?,
    label: String,
    maxLength: Int = 255,
    onValueChange: (String?) -> Unit,
) {
    val fontSize = rememberFontSize()
    val labelFontSize = getLabelFontSize()

    OutlinedTextField(
        value = value.orEmpty(),
        onValueChange = {
            val truncated = it.take(maxLength)
            onValueChange(truncated.ifBlank { null })
        },
        textStyle = TextStyle(fontSize = fontSize),
        label = { Text(label, color = AppTheme.PrimaryGreen, fontSize = labelFontSize) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppTheme.PrimaryGreen,
            unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
            focusedTextColor = AppTheme.PrimaryGreen,
            unfocusedTextColor = AppTheme.PrimaryGreen,
            cursorColor = AppTheme.PrimaryGreen
        )
    )
}