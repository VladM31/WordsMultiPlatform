package vm.words.ua.exercise.ui.componets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.ImageFromBytes
import vm.words.ua.core.utils.getImageSize
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.image_icon

@Composable
fun ExerciseImageView(
    enableImage: Boolean,
    word: ExerciseWordDetails,
    fontSize: androidx.compose.ui.unit.TextUnit,
    toText: (ExerciseWordDetails) -> String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (enableImage) {
            val imageSize = getImageSize()
            ImageFromBytes(
                imageBytes = word.imageContent?.bytes,
                defaultPaint = painterResource(Res.drawable.image_icon),
                width = imageSize,
                height = imageSize,
                contentDescription = "Word Image"
            )
            Spacer(Modifier.height(10.dp))
        }

        Text(
            text = toText(word),
            color = AppTheme.PrimaryColor,
            textAlign = TextAlign.Center,
            fontSize = fontSize,
            style = LocalTextStyle.current.copy(lineHeight = fontSize * 1.1f),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
    }
}