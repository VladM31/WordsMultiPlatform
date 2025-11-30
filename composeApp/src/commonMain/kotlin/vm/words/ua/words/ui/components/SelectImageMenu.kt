package vm.words.ua.words.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.name
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.ImageFromPlatformFile
import vm.words.ua.core.utils.getImageSize
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete

@Composable
fun SelectImageMenu(
    image: PlatformFile? = null,
    imagePicker: PickerResultLauncher,
    title: String,
    underText: String? = null,
    onRemoveImage: () -> Unit,

    ) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            color = AppTheme.PrimaryColor,
            fontSize = rememberFontSize()
        )

        image?.let {
            ImageFromPlatformFile(
                file = it,
                modifier = Modifier
                    .heightIn(max = getImageSize()) // apply height constraint first
                    .fillMaxWidth()
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
            )
        }



        Row(
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            OutlinedButton(
                onClick = { imagePicker.launch() },
                modifier = Modifier.weight(3f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppTheme.PrimaryGreen
                )
            ) {
                Text(
                    text = if (image != null) "Change Image" else "Select Image",
                    fontSize = rememberFontSize()
                )
            }
            if (image == null) {
                return@Row
            }
            IconButton(
                onClick = onRemoveImage,
                modifier = Modifier
                    .size(rememberIconSize())
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.delete),
                    contentDescription = "Remove",
                    tint = AppTheme.PrimaryGreen,
                    modifier = Modifier.size(rememberIconSize())
                )
            }
        }

        image?.let { file ->
            Text(
                text = "Selected: ${file.name}",
                color = AppTheme.PrimaryGreen,
                fontSize = rememberFontSize() * 0.85f,
                lineHeight = rememberFontSize() * 1.1f
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