package vm.words.ua.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.arrow
import wordsmultiplatform.composeapp.generated.resources.setting
import vm.words.ua.navigation.SimpleNavController

@Composable
fun AppToolBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onAdditionalClick: (() -> Unit)? = null,
    showBackButton: Boolean = true,
    showAdditionalButton: Boolean = false,
    additionalButtonImage: Painter = painterResource(Res.drawable.setting)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(AppTheme.PrimaryBack)
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            BackButton(
                onBackClick = onBackClick,
                showBackButton = showBackButton
            )

            // Title
            Text(
                text = title,
                color = AppTheme.PrimaryColor,
                fontSize = 29.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            // Additional Button
            AdditionalButton(
                onAdditionalClick = onAdditionalClick,
                showAdditionalButton = showAdditionalButton,
                additionalButtonImage = additionalButtonImage
            )
        }
    }
}

@Composable
private fun BackButton(
    onBackClick: (() -> Unit)?,
    showBackButton: Boolean,
){
    if (showBackButton.not()){
        Box(modifier = Modifier.size(40.dp))
        return
    }

    IconButton(
        onClick = onBackClick ?: {},
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.arrow),
            contentDescription = "Back",
            tint = AppTheme.PrimaryColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun AdditionalButton(
    onAdditionalClick: (() -> Unit)? = null,
    showAdditionalButton: Boolean = false,
    additionalButtonImage: Painter = painterResource(Res.drawable.setting)
){
    if (showAdditionalButton.not()){
        Box(modifier = Modifier.size(40.dp))
        return
    }

    IconButton(
        onClick = onAdditionalClick ?: {},
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            painter = additionalButtonImage,
            contentDescription = "More",
            tint = AppTheme.PrimaryColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Overload: accept a SimpleNavController and wire the back button to it
@Composable
fun AppToolBar(
    title: String,
    navController: SimpleNavController,
    modifier: Modifier = Modifier,
    onAdditionalClick: (() -> Unit)? = null,
    showBackButton: Boolean = true,
    showAdditionalButton: Boolean = false
) {
    AppToolBar(
        title = title,
        modifier = modifier,
        onBackClick = { navController.popBackStack() },
        onAdditionalClick = onAdditionalClick,
        showBackButton = showBackButton,
        showAdditionalButton = showAdditionalButton
    )
}
