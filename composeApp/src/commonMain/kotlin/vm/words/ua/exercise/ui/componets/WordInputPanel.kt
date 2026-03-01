package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import vm.words.ua.core.ui.AppTheme

@Composable
fun WordInputPanel(
    text: String,
    onTextChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onDone: (() -> Unit)? = null,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        vm.words.ua.core.ui.components.TextInput(
            value = text,
            label = "Enter word",
            onValueChange = {
                onTextChange(it.orEmpty())
            },
            enabled = enabled,
            color = if (isError) AppTheme.Error else AppTheme.PrimaryColor,
            keyboardOptions = KeyboardOptions(imeAction = if (onDone != null) ImeAction.Done else ImeAction.Default),
            keyboardActions = KeyboardActions(onDone = { onDone?.invoke() }),
            modifier = if (onDone != null) Modifier.onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.Enter || event.key == Key.NumPadEnter || event.key == Key.Tab)
                ) {
                    onDone()
                    true
                } else false
            } else Modifier,
        )
    }
}

