package vm.words.ua.words.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.name
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getIconButtonSize
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize

@Composable
fun SelectSoundMenu(
    sound: PlatformFile? = null,
    soundPicker: PickerResultLauncher,
    title: String,
    underText: String? = null,
    isPlay: Boolean = false,
    onPlayClick: () -> Unit = {},
    onRemoveSound: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            color = AppTheme.PrimaryColor,
            fontSize = rememberFontSize()
        )

        sound?.let {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onPlayClick,
                    enabled = isPlay.not(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlay) AppTheme.PrimaryGray else AppTheme.PrimaryColor,
                    ),
                    modifier = Modifier
                        .size(getIconButtonSize() * 1.5f)
                ) {
                    Icon(
                        imageVector = if (isPlay) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.PlayArrow,
                        contentDescription = "Play sound",
                        tint = AppTheme.PrimaryBack,
                        modifier = Modifier.size(rememberIconSize() * 1.5f)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            OutlinedButton(
                onClick = { soundPicker.launch() },
                modifier = Modifier.weight(3f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppTheme.PrimaryColor
                )
            ) {
                Text(
                    text = if (sound != null) "Change Sound" else "Select Sound",
                    fontSize = rememberFontSize()
                )
            }
            if (sound == null) {
                return@Row
            }
            IconButton(
                onClick = onRemoveSound,
                modifier = Modifier
                    .size(rememberIconSize())
                    .weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = AppTheme.PrimaryColor,
                    modifier = Modifier.size(rememberIconSize())
                )
            }
        }

        sound?.let { file ->
            Text(
                text = "Selected: ${file.name}",
                color = AppTheme.PrimaryColor,
                fontSize = rememberFontSize() * 0.85f,
                lineHeight = rememberFontSize()
            )
        }

        underText?.let {
            Text(
                text = it,
                color = AppTheme.PrimaryColor.copy(alpha = 0.6f),
                fontSize = rememberFontSize() * 0.85f,
                lineHeight = rememberFontSize() * 1.1f
            )
        }
    }
}