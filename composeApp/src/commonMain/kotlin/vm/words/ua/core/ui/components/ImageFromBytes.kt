package vm.words.ua.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.decodeToImageBitmap
import io.ktor.utils.io.core.toByteArray
import org.jetbrains.compose.resources.painterResource
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.arrow
import wordsmultiplatform.composeapp.generated.resources.image_icon


@Composable
fun ImageFromBytes(
    imageBytes: ByteArray? = null,
    defaultPaint: Painter = painterResource(Res.drawable.image_icon),
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    width: Dp = 200.dp,
    height: Dp = 200.dp,
    contentDescription: String = "Image"
) {
    var imageBitmap by remember(imageBytes) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(imageBytes) { mutableStateOf(false) }

    LaunchedEffect(imageBytes) {
        if (imageBytes != null && imageBytes.isNotEmpty()) {
            isLoading = true
            try {
                imageBitmap = imageBytes.decodeToImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
                imageBitmap = null
            } finally {
                isLoading = false
            }
        } else {
            imageBitmap = null
            isLoading = false
        }
    }

    Box(
        modifier = modifier.size(width, height),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            imageBitmap != null -> {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.size(width, height)
                )
            }
            imageBytes == null -> {
                Image(
                    painter = defaultPaint,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.size(width, height)
                )
            }
        }
    }
}

/**
 * Более простая версия для базового использования
 */
@Composable
fun SimpleImageFromBytes(
    imageBytes: ByteArray?,
    contentDescription: String = "Image",
    modifier: Modifier = Modifier
) {
    var imageBitmap by remember(imageBytes) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageBytes) {
        if (imageBytes != null && imageBytes.isNotEmpty()) {
            try {
                imageBitmap = imageBytes.decodeToImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
}

