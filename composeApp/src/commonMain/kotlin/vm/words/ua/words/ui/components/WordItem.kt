package vm.words.ua.words.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.toFormatDateTime
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word


@Composable
fun WordItem(
    word: Word,
    userWord: UserWord? = null,
    notSelectedIcon: ImageVector? = Icons.Outlined.AddCircle,
    selectedIcon: ImageVector = Icons.Filled.Check,
    isSelected: Boolean, // Cart state (isInCart)
    onSelect: () -> Unit, // Cart toggle
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scaleFactor = getScaleFactor(maxWidth)

        // Reactive colors
        val cardBackground = AppColors.secondaryBack
        val accentPrimary = AppColors.primaryColor
        val accentSecondary = AppColors.secondaryColor
        val textPrimary = AppColors.primaryText
        val textSecondary = AppColors.secondaryText
        val textMuted = AppColors.primaryDisable

        val titleSize = (18 * scaleFactor).sp
        val translateSize = (16 * scaleFactor).sp
        val dateSize = (11 * scaleFactor).sp
        val iconSize = (20 * scaleFactor).dp
        val cardPadding = (16 * scaleFactor).dp
        val horizontalPadding = (8 * scaleFactor).dp
        val verticalPadding = (6 * scaleFactor).dp

        // State for meta info visibility
        val hasMetaInfo = userWord?.createdAt != null || userWord?.lastReadDate != null
        var showMetaInfo by remember { mutableStateOf(false) }

        // Cart animation
        val cartBorderColor by animateColorAsState(
            targetValue = if (isSelected) accentPrimary else Color.Transparent,
            animationSpec = tween(400)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.15f),
                    spotColor = Color.Black.copy(alpha = 0.15f)
                )
                .border(
                    width = 3.dp,
                    color = cartBorderColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { onSelect() } // Long press for cart
                    )
                },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardBackground
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                cardBackground,
                                cardBackground
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(cardPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        // Main content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        ) {
                            // Original word
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LanguageBadge(
                                    langCode = word.lang.upperShortName,
                                    color = accentPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = word.original,
                                    color = textPrimary,
                                    fontSize = titleSize,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Translation
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LanguageBadge(
                                    langCode = word.translateLang.upperShortName,
                                    color = accentSecondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = word.translate,
                                    color = textSecondary,
                                    fontSize = translateSize,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                            // Category
                            word.category?.let { category ->
                                Spacer(modifier = Modifier.height(8.dp))
                                CategoryChip(category = category, accentColor = accentPrimary)
                            }
                        }

                        // Right panel with buttons
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Buttons in one row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Cart button
                                notSelectedIcon?.let {
                                    SelectButton(
                                        isSelected = isSelected,
                                        onClick = onSelect,
                                        size = iconSize * 1.8f,
                                        notSelectedImage = it,
                                        selectedIcon = selectedIcon,
                                        accentColor = accentPrimary,
                                        buttonBackground = cardBackground,
                                        textSecondaryColor = textSecondary
                                    )
                                }


                                // Open button
                                OpenButton(
                                    onClick = onOpen,
                                    size = iconSize * 1.8f,
                                    accentColor = accentPrimary
                                )
                            }

                            // Media indicators and Info button row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Info button for meta information
                                if (hasMetaInfo) {
                                    InfoButton(
                                        isExpanded = showMetaInfo,
                                        onClick = { showMetaInfo = !showMetaInfo },
                                        size = iconSize * 1.2f,
                                        accentColor = accentSecondary,
                                        buttonBackground = cardBackground,
                                        textSecondaryColor = textSecondary
                                    )
                                }

                                // Media indicators
                                MediaIndicators(
                                    hasImage = word.imageLink != null,
                                    hasSound = word.soundLink != null,
                                    iconSize = iconSize * 0.9f,
                                    accentColor = accentPrimary,
                                    secondaryColor = accentSecondary,
                                    buttonBackground = cardBackground
                                )
                            }
                        }
                    }

                    // Meta info section at the bottom (expandable)
                    if (!(hasMetaInfo && showMetaInfo)) {
                        return@Box
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = cardBackground.copy(alpha = 0.5f)
                            )
                            .padding(horizontal = cardPadding, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        userWord?.createdAt?.let {
                            MetaInfoText(
                                label = "Added",
                                value = it.toFormatDateTime(),
                                fontSize = dateSize,
                                mutedColor = textMuted,
                                secondaryColor = textSecondary
                            )
                        }
                        userWord?.lastReadDate?.let {
                            MetaInfoText(
                                label = "Last read",
                                value = it.toFormatDateTime(),
                                fontSize = dateSize,
                                mutedColor = textMuted,
                                secondaryColor = textSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageBadge(
    langCode: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = langCode,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CategoryChip(category: String, accentColor: Color) {
    Box(
        modifier = Modifier
            .background(
                color = accentColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = category,
            color = accentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MetaInfoText(
    label: String,
    value: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    mutedColor: Color,
    secondaryColor: Color
) {
    Column {
        Text(
            text = label,
            color = mutedColor,
            fontSize = fontSize * 0.9f
        )
        Text(
            text = value,
            color = secondaryColor.copy(alpha = 0.7f),
            fontSize = fontSize
        )
    }
}

@Composable
private fun SelectButton(
    notSelectedImage: ImageVector,
    selectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp,
    accentColor: Color,
    buttonBackground: Color,
    textSecondaryColor: Color
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            accentColor.copy(alpha = 0.2f)
        else
            buttonBackground,
        animationSpec = tween(200)
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected)
            accentColor
        else
            textSecondaryColor,
        animationSpec = tween(200)
    )

    val buttonScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .scale(buttonScale),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        contentColor = iconColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = selectedIcon,
                    contentDescription = "",
                    modifier = Modifier.size(size * 0.5f)
                )
                return@Box
            }

            Icon(
                imageVector = notSelectedImage,
                contentDescription = "",
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}

@Composable
private fun InfoButton(
    isExpanded: Boolean,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp,
    accentColor: Color,
    buttonBackground: Color,
    textSecondaryColor: Color
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isExpanded)
            accentColor.copy(alpha = 0.2f)
        else
            buttonBackground.copy(alpha = 0.6f),
        animationSpec = tween(200)
    )

    val iconColor by animateColorAsState(
        targetValue = if (isExpanded)
            accentColor
        else
            textSecondaryColor,
        animationSpec = tween(200)
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        contentColor = iconColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Show info",
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

@Composable
private fun OpenButton(
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp,
    accentColor: Color
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(12.dp),
        color = accentColor.copy(alpha = 0.15f),
        contentColor = accentColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "Open word",
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}

@Composable
private fun MediaIndicators(
    hasImage: Boolean,
    hasSound: Boolean,
    iconSize: androidx.compose.ui.unit.Dp,
    accentColor: Color,
    secondaryColor: Color,
    buttonBackground: Color
) {
    if (!hasImage && !hasSound) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(
                color = buttonBackground.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (hasImage) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = "Has image",
                tint = accentColor.copy(alpha = 0.8f),
                modifier = Modifier.size(iconSize)
            )
        }

        if (hasSound) {
            Icon(
                imageVector = Icons.Outlined.VolumeUp,
                contentDescription = "Has sound",
                tint = secondaryColor.copy(alpha = 0.8f),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
