package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberLabelFontSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SingleSelectInput(
    value: T?,
    items: List<T>,
    label: String,
    toLabel: (T) -> String,
    showNone: Boolean = true,
    noneLabel: String = "",
    onSelect: (T?) -> Unit,
    modifier: Modifier = Modifier,
    // Optional helper text shown below the input
    helperText: String? = null,
) {
    val fontSize = rememberFontSize()
    val labelFontSize = rememberLabelFontSize()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value?.let(toLabel) ?: noneLabel,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle(fontSize = fontSize),
            label = { Text(label, color = AppTheme.PrimaryGreen, fontSize = labelFontSize) },
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.PrimaryGreen,
                unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                focusedTextColor = AppTheme.PrimaryGreen,
                unfocusedTextColor = AppTheme.PrimaryGreen,
                cursorColor = AppTheme.PrimaryGreen
            ),
            // Render helper text below the field when provided
            supportingText = if (helperText != null) {
                {
                    Text(
                        helperText,
                        color = AppTheme.PrimaryGreen.copy(alpha = 0.7f),
                        fontSize = rememberLabelFontSize() * 0.65
                    )
                }
            } else null
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (showNone) {
                DropdownMenuItem(
                    text = { Text(noneLabel, color = AppTheme.PrimaryGreen, fontSize = labelFontSize) },
                    onClick = {
                        onSelect(null)
                        expanded = false
                    }
                )
            }
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(toLabel(item), color = AppTheme.PrimaryGreen, fontSize = labelFontSize) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
