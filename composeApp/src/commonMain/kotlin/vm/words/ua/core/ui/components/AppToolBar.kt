package vm.words.ua.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.utils.hints.ui.utils.ViewHintStep
import vm.words.ua.utils.hints.ui.utils.viewHint
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.arrow
import wordsmultiplatform.composeapp.generated.resources.setting
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Position of the tooltip relative to the button
 */
enum class TooltipPosition {
    TOP,    // Above the button
    BOTTOM, // Below the button
    LEFT,   // To the left of the button
    RIGHT   // To the right of the button
}

/**
 * Tooltip configuration for additional button
 * @param text Text to display in tooltip
 * @param duration How long to show the tooltip before it disappears
 * @param position Position of the tooltip relative to the button
 */
data class AdditionalButtonTooltip(
    val text: String,
    val duration: Duration = 3.seconds,
    val position: TooltipPosition = TooltipPosition.BOTTOM
)

@Composable
fun AppToolBar(
    title: String,
    modifier: Modifier = Modifier,
    backButtonImage: Painter = painterResource(Res.drawable.arrow),
    backButtonVector: ImageVector? = null,
    onBackClick: (() -> Unit)? = null,
    onAdditionalClick: (() -> Unit)? = null,
    showBackButton: Boolean = true,
    showAdditionalButton: Boolean = false,
    additionalButtonVector: ImageVector? = null,
    additionalButtonImage: Painter = painterResource(Res.drawable.setting),
    currentStepHint: ViewHintStep? = null,
    additionalButtonStepHint: ViewHintStep? = null,
    additionalButtonTooltip: AdditionalButtonTooltip? = null,
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
            // Main toolbar content layer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(0f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                BackButton(
                    onBackClick = onBackClick,
                    showBackButton = showBackButton,
                    buttonSize = buttonSize,
                    iconSize = iconSize,
                    backButtonImage = backButtonImage,
                    backButtonVector = backButtonVector
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
                    additionalButtonVector = additionalButtonVector,
                    buttonSize = buttonSize,
                    modifier = additionalButtonStepHint?.let {
                        additionalButtonModifier.viewHint(
                            it,
                            currentStepHint
                        )
                    } ?: additionalButtonModifier
                )
            }

            // Tooltip overlay layer - positioned above toolbar content
            if (showAdditionalButton && additionalButtonTooltip != null) {
                AdditionalButtonTooltipView(
                    tooltip = additionalButtonTooltip,
                    scaleFactor = scaleFactor,
                    toolbarHeight = toolbarHeight,
                    modifier = Modifier.zIndex(1f)
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
    backButtonVector: ImageVector?,
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
        if (backButtonVector != null) {
            Icon(
                imageVector = backButtonVector,
                contentDescription = "Back",
                tint = AppTheme.PrimaryColor,
                modifier = Modifier.size(iconSize)
            )
        } else {
            Icon(
                painter = backButtonImage,
                contentDescription = "Back",
                tint = AppTheme.PrimaryColor,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
private fun AdditionalButton(
    onAdditionalClick: (() -> Unit)? = null,
    showAdditionalButton: Boolean = false,
    additionalButtonImage: Painter = painterResource(Res.drawable.setting),
    additionalButtonVector: ImageVector?,
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
        if (additionalButtonVector != null) {
            Icon(
                imageVector = additionalButtonVector,
                contentDescription = "More",
                tint = AppTheme.PrimaryColor,
                modifier = modifier
            )
            return@IconButton
        }

        Icon(
            painter = additionalButtonImage,
            contentDescription = "More",
            tint = AppTheme.PrimaryColor,
            modifier = modifier
        )
    }
}

@Composable
private fun BoxScope.AdditionalButtonTooltipView(
    tooltip: AdditionalButtonTooltip,
    scaleFactor: Float,
    toolbarHeight: Dp,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(tooltip) {
        isVisible = true
        delay(tooltip.duration)
        isVisible = false
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .align(
                when (tooltip.position) {
                    TooltipPosition.TOP -> Alignment.TopEnd
                    TooltipPosition.BOTTOM -> Alignment.BottomEnd
                    TooltipPosition.LEFT -> Alignment.CenterEnd
                    TooltipPosition.RIGHT -> Alignment.CenterEnd
                }
            )
            .offset(
                x = when (tooltip.position) {
                    TooltipPosition.LEFT -> -(60 * scaleFactor).dp
                    TooltipPosition.RIGHT -> (60 * scaleFactor).dp
                    else -> 0.dp
                },
                y = when (tooltip.position) {
                    TooltipPosition.TOP -> -(toolbarHeight * 0.3f)
                    TooltipPosition.BOTTOM -> (toolbarHeight * 0.3f)
                    else -> 0.dp
                }
            )
    ) {
        Text(
            text = tooltip.text,
            color = AppTheme.PrimaryColor,
            fontSize = (12 * scaleFactor).sp,
            textAlign = TextAlign.Center,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .widthIn(max = (200 * scaleFactor).dp)
                .background(
                    color = AppTheme.PrimaryBack.copy(alpha = 0.9f),
                    shape = RoundedCornerShape((6 * scaleFactor).dp)
                )
                .padding(
                    horizontal = (8 * scaleFactor).dp,
                    vertical = (4 * scaleFactor).dp
                )
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
