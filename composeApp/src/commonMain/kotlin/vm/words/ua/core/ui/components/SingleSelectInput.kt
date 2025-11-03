package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getLabelFontSize

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
) {
    val fontSize = getFontSize()
    val labelFontSize = getLabelFontSize()
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
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.PrimaryGreen,
                unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                focusedTextColor = AppTheme.PrimaryGreen,
                unfocusedTextColor = AppTheme.PrimaryGreen,
                cursorColor = AppTheme.PrimaryGreen
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (showNone) {
                DropdownMenuItem(
                    text = { Text(noneLabel, color = AppTheme.PrimaryGreen) },
                    onClick = {
                        onSelect(null)
                        expanded = false
                    }
                )
            }
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(toLabel(item), color = AppTheme.PrimaryGreen) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

