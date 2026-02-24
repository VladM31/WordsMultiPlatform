package vm.words.ua.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import vm.words.ua.core.utils.rememberInterfaceMaxWidth
import vm.words.ua.navigation.LocalSwipeRightOverride
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.utils.hints.ui.utils.ViewHintStep
import vm.words.ua.utils.hints.ui.utils.viewHint
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.setting
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


enum class TooltipPosition {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT
}


data class AdditionalButtonTooltip(
    val text: String,
    val duration: Duration = 3.seconds,
    val position: TooltipPosition = TooltipPosition.BOTTOM
)

@Composable
fun AppToolBar(
    title: String,
    modifier: Modifier = Modifier,
    backButtonVector: ImageVector? = null,
    onBackClick: (() -> Unit)? = null,
    onLeftSwipe: (() -> Unit)? = onBackClick,
    onAdditionalClick: (() -> Unit)? = null,
    showBackButton: Boolean = true,
    showAdditionalButton: Boolean = false,
    additionalButtonVector: ImageVector? = null,
    additionalButtonImage: Painter = painterResource(Res.drawable.setting),
    currentStepHint: ViewHintStep? = null,
    additionalButtonStepHint: ViewHintStep? = null,
    additionalButtonTooltip: AdditionalButtonTooltip? = null,
) {
    val swipeOverride = LocalSwipeRightOverride.current
    DisposableEffect(onLeftSwipe) {
        onDispose { swipeOverride.value = null }
    }
    LaunchedEffect(onLeftSwipe){
        swipeOverride.value = onLeftSwipe
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.PrimaryBack),
        contentAlignment = Alignment.Center
    ) {
        val scaleFactor = getScaleFactor(maxWidth)

        val toolbarHeight = (56 * scaleFactor).dp
        val horizontalPadding = (10 * scaleFactor).dp
        val titleSize = (26 * scaleFactor).sp
        val titleHorizontalPadding = (16 * scaleFactor).dp

        val iconSize = (56 * scaleFactor).dp
        val buttonSize = iconSize * 1.05f
        val additionalButtonModifier = Modifier.size(iconSize)

        Box(
            modifier = Modifier
                .widthIn(max = rememberInterfaceMaxWidth())
                .fillMaxWidth()
                .height(toolbarHeight)
                .padding(horizontal = horizontalPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(0f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onBackClick = onBackClick,
                    showBackButton = showBackButton,
                    buttonSize = buttonSize,
                    iconSize = iconSize,
                    backButtonVector = backButtonVector
                )

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
        Icon(
            imageVector = backButtonVector ?: Icons.AutoMirrored.Filled.ArrowBack,
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