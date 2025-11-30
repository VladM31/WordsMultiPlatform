package vm.words.ua.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getLabelFontSize
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.add

/**
 * A reusable component for managing a list of strings with add/remove functionality
 *
 * @param label The label for the input field
 * @param items The current list of items
 * @param onItemsChange Callback when the list changes
 * @param modifier Modifier for the component
 * @param maxLength Maximum length for each item (default: 255)
 * @param removeButtonText Text for the remove button (default: "Remove")
 */
@Composable
fun StringListInput(
    label: String,
    items: Collection<String>?,
    onItemsChange: (List<String>?) -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int = 255,
    removeButtonText: String = "Remove"
) {
    var inputValue by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputValue,
                onValueChange = {
                    val newText = it.text.take(maxLength)
                    val newSelectionEnd = minOf(it.selection.end, newText.length)
                    val newSelectionStart = minOf(it.selection.start, newText.length)
                    inputValue = TextFieldValue(
                        text = newText,
                        selection = TextRange(newSelectionStart, newSelectionEnd)
                    )
                },
                label = { Text(label, color = AppTheme.PrimaryGreen, fontSize = getLabelFontSize()) },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontSize = rememberFontSize()),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.PrimaryGreen,
                    unfocusedBorderColor = AppTheme.PrimaryGreen.copy(alpha = 0.5f),
                    focusedTextColor = AppTheme.PrimaryGreen,
                    unfocusedTextColor = AppTheme.PrimaryGreen,
                    cursorColor = AppTheme.PrimaryGreen
                )
            )
            IconButton(
                onClick = {
                    val value = inputValue.text.trim()
                    val current = items?.toMutableList() ?: mutableListOf()
                    if (value.isNotEmpty() && !current.contains(value)) {
                        current.add(value)
                        onItemsChange(current)
                        inputValue = TextFieldValue("")
                    }
                },
                modifier = Modifier.size(rememberIconSize())
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add),
                    contentDescription = "Add",
                    tint = AppTheme.PrimaryGreen
                )
            }
        }

        val currentItems = items ?: emptyList()
        if (currentItems.isEmpty()) {
            return
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            currentItems.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item, color = AppTheme.PrimaryGreen, fontSize = rememberFontSize())
                    OutlinedButton(
                        onClick = {
                            val updated = currentItems.toMutableList().also { it.remove(item) }
                            onItemsChange(if (updated.isEmpty()) null else updated)
                        }
                    ) {
                        Text(removeButtonText, color = AppTheme.PrimaryGreen, fontSize = rememberFontSize())
                    }
                }
            }
        }
    }
}

