package vm.words.ua.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.painterResource
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.image_icon


@Composable
fun ImageFromPlatformFile(
    file: PlatformFile,
    defaultPaint: Painter = painterResource(Res.drawable.image_icon),
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    width: Dp = 200.dp,
    height: Dp = 200.dp,
    contentDescription: String = "Image"
) {

    var imageBytes by remember(file) { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(file) {
        try {
            imageBytes = file.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            imageBytes = null
        }
    }

    ImageFromBytes(
        imageBytes = imageBytes,
        defaultPaint = defaultPaint,
        modifier = modifier,
        contentScale = contentScale,
        width = width,
        height = height,
        contentDescription = contentDescription
    )
}

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

    val finalModifier = if (modifier == Modifier) Modifier.size(width, height) else modifier

    Box(
        modifier = finalModifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            imageBitmap != null -> {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
            imageBytes == null -> {
                Image(
                    painter = defaultPaint,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}