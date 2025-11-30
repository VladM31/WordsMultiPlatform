package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.TextInput
import vm.words.ua.core.utils.getLabelFontSize
import vm.words.ua.core.utils.rememberFontSize

/**
 * Dialog for creating a new playlist.
 * @param onDismiss invoked when dialog should be closed without creating
 * @param onCreate invoked with playlist name when user presses create
 */
@Composable
fun CreatePlayListDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text("Create Playlist", color = AppTheme.PrimaryColor, fontSize = rememberFontSize())
                Spacer(Modifier.height(12.dp))
                TextInput(
                    label = "Name",
                    value = name,
                    onValueChange = {
                        name = it.orEmpty()
                        if (error != null) error = null
                    }
                )

                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = rememberFontSize()
                    )
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancel", fontSize = getLabelFontSize())
                    }
                    Spacer(Modifier.padding(horizontal = 6.dp))
                    Button(onClick = {
                        val trimmed = name.trim()
                        if (trimmed.isEmpty()) {
                            error = "Name cannot be empty"
                            return@Button
                        }
                        onCreate(trimmed)
                        onDismiss()
                    }) {
                        Text("Create", fontSize = getLabelFontSize())
                    }
                }
            }
        }
    }
}

