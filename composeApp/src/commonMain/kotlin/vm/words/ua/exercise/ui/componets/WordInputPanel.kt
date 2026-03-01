package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import vm.words.ua.core.ui.AppTheme

@Composable
fun WordInputPanel(
    text: String,
    onTextChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    ) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        vm.words.ua.core.ui.components.TextInput(
            value = text,
            label = "Enter word",
            onValueChange = {
                onTextChange(it.orEmpty())
            },
            enabled = enabled,
            color = if (isError) AppTheme.Error else AppTheme.PrimaryColor
        )
    }
}

