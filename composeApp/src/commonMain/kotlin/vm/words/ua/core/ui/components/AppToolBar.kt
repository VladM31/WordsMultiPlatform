package vm.words.ua.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.hints.ui.utils.ViewHintStep
import vm.words.ua.hints.ui.utils.viewHint
import vm.words.ua.navigation.SimpleNavController
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.arrow
import wordsmultiplatform.composeapp.generated.resources.setting

@Composable
fun AppToolBar(
    title: String,
    modifier: Modifier = Modifier,
    backButtonImage: Painter = painterResource(Res.drawable.arrow),
    onBackClick: (() -> Unit)? = null,
    onAdditionalClick: (() -> Unit)? = null,
    showBackButton: Boolean = true,
    showAdditionalButton: Boolean = false,
    additionalButtonImage: Painter = painterResource(Res.drawable.setting),
    currentStepHint: ViewHintStep? = null,
    additionalButtonStepHint: ViewHintStep? = null,
) {


    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryBack)
    ) {
        val scaleFactor = getScaleFactor(maxWidth)

        val toolbarHeight = (56 * scaleFactor).dp
        val horizontalPadding = (10 * scaleFactor).dp
        val titleSize = (26 * scaleFactor).sp
        val titleHorizontalPadding = (16 * scaleFactor).dp

        val iconSize = (40 * scaleFactor).dp
        val buttonSize = iconSize * 1.2f
        val additionalButtonModifier = Modifier.size(iconSize)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeight)
                .padding(horizontal = horizontalPadding),
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
                    showBackButton = showBackButton,
                    buttonSize = buttonSize,
                    iconSize = iconSize,
                    backButtonImage = backButtonImage
                )

                // Title
                Text(
                    text = title,
                    color = AppTheme.PrimaryColor,
                    fontSize = titleSize,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = titleHorizontalPadding)
                )

                // Additional Button
                AdditionalButton(
                    onAdditionalClick = onAdditionalClick,
                    showAdditionalButton = showAdditionalButton,
                    additionalButtonImage = additionalButtonImage,
                    buttonSize = buttonSize,
                    modifier = additionalButtonStepHint?.let {
                        additionalButtonModifier.viewHint(
                            it,
                            currentStepHint
                        )
                    } ?: additionalButtonModifier
                )
            }
        }
    }
}

@Composable
private fun BackButton(
    onBackClick: (() -> Unit)?,
    showBackButton: Boolean,
    backButtonImage: Painter,
    buttonSize: Dp,
    iconSize: Dp
) {
    if (showBackButton.not()) {
        Box(modifier = Modifier.size(buttonSize))
        return
    }

    IconButton(
        onClick = onBackClick ?: {},
        modifier = Modifier.size(buttonSize)
    ) {
        Icon(
            painter = backButtonImage,
            contentDescription = "Back",
            tint = AppTheme.PrimaryColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
private fun AdditionalButton(
    onAdditionalClick: (() -> Unit)? = null,
    showAdditionalButton: Boolean = false,
    additionalButtonImage: Painter = painterResource(Res.drawable.setting),
    buttonSize: Dp,
    modifier: Modifier
) {
    if (showAdditionalButton.not()) {
        Box(modifier = Modifier.size(buttonSize))
        return
    }

    IconButton(
        onClick = onAdditionalClick ?: {},
        modifier = Modifier.size(buttonSize)
    ) {
        Icon(
            painter = additionalButtonImage,
            contentDescription = "More",
            tint = AppTheme.PrimaryColor,
            modifier = modifier
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
