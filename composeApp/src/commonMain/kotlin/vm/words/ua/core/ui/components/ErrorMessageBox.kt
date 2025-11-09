package vm.words.ua.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getScaleFactor

@Composable
fun ErrorMessageBox(
    message: ErrorMessage,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    asDialog: Boolean = true
) {
    if (asDialog) {
        // remember visibility per message id so new messages re-open the dialog
        ErrorMessageAlertDialog(
            message = message,
            modifier = modifier,
            onDismiss = onDismiss
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, AppTheme.PrimaryColor, RoundedCornerShape(8.dp))
            .background(
                color = AppTheme.PrimaryBack,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = message.message,
            color = AppTheme.PrimaryColor,
            fontSize = getFontSize(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ErrorMessageAlertDialog(
    message: ErrorMessage,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    var visible by remember(message.id) { mutableStateOf(true) }
    val scale = getScaleFactor()

    val fontSize = remember {
        getFontSize(scale) * 0.85
    }
    val heightFontSize = remember {
        getFontSize(scale) * 0.92
    }

    if (visible.not()) {
        return
    }


    AlertDialog(
        onDismissRequest = {
            visible = false
            onDismiss?.invoke()
        },
        confirmButton = {
            TextButton(onClick = {
                visible = false
                onDismiss?.invoke()
            }) {
                Text(
                    text = "OK",
                    color = AppTheme.PrimaryColor,
                    fontSize = getFontSize(),
                )
            }
        },
        title = null,
        text = {
            Text(
                text = message.message,
                color = AppTheme.PrimaryColor,
                fontSize = fontSize,
                textAlign = TextAlign.Center,
                lineHeight = heightFontSize,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier.border(1.dp, AppTheme.PrimaryColor, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        containerColor = AppTheme.PrimaryBack
    )
}