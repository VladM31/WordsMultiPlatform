package vm.words.ua.words.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.toFormatDateTime
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.arrow
import wordsmultiplatform.composeapp.generated.resources.check_mark
import wordsmultiplatform.composeapp.generated.resources.image_icon
import wordsmultiplatform.composeapp.generated.resources.sound

// Кольорова палітра на основі AppTheme
private object WordItemColors {
    val CardBackground = AppTheme.PrimaryBackLight
    val CardBackgroundGradientEnd = AppTheme.PrimaryBackLight
    val AccentCyan = AppTheme.PrimaryGreen
    val AccentPurple = AppTheme.PrimaryViolet
    val AccentGreen = AppTheme.PrimaryGreen
    val AccentPink = AppTheme.PrimaryRed
    val TextPrimary = AppTheme.PrimaryText
    val TextSecondary = AppTheme.SecondaryText
    val TextMuted = AppTheme.PrimaryDisable
    val ButtonBackground = AppTheme.SecondaryBack
    val CartActive = AppTheme.PrimaryGreen
}

@Composable
fun WordItem(
    word: Word,
    userWord: UserWord? = null,
    isSelected: Boolean, // Це стан корзини (isInCart)
    onSelect: () -> Unit, // Це toggle корзини
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scaleFactor = getScaleFactor(maxWidth)

        val titleSize = (18 * scaleFactor).sp
        val translateSize = (16 * scaleFactor).sp
        val categorySize = (13 * scaleFactor).sp
        val dateSize = (11 * scaleFactor).sp
        val iconSize = (20 * scaleFactor).dp
        val cardPadding = (16 * scaleFactor).dp
        val horizontalPadding = (8 * scaleFactor).dp
        val verticalPadding = (6 * scaleFactor).dp

        // Анімація для корзини
        val cartBorderColor by animateColorAsState(
            targetValue = if (isSelected) WordItemColors.CartActive else Color.Transparent,
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
                        onLongPress = { onSelect() } // Long press для корзини
                    )
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.SecondaryBack
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                WordItemColors.CardBackground,
                                WordItemColors.CardBackgroundGradientEnd
                            )
                        )
                    )
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кольоровий індикатор зліва
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        WordItemColors.AccentCyan,
                                        WordItemColors.AccentPurple
                                    )
                                )
                            )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Основний контент
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        // Оригінальне слово
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LanguageBadge(
                                langCode = word.lang.upperShortName,
                                color = WordItemColors.AccentCyan
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = word.original,
                                color = WordItemColors.TextPrimary,
                                fontSize = titleSize,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Переклад
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LanguageBadge(
                                langCode = word.translateLang.upperShortName,
                                color = WordItemColors.AccentPurple
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = word.translate,
                                color = WordItemColors.TextSecondary,
                                fontSize = translateSize,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        // Категорія
                        word.category?.let { category ->
                            Spacer(modifier = Modifier.height(8.dp))
                            CategoryChip(category = category)
                        }

                        // Мета-інформація
                        val hasMetaInfo = userWord?.createdAt != null || userWord?.lastReadDate != null
                        if (hasMetaInfo) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                userWord?.createdAt?.let {
                                    MetaInfoText(
                                        label = "Додано",
                                        value = it.toFormatDateTime(),
                                        fontSize = dateSize
                                    )
                                }
                                userWord?.lastReadDate?.let {
                                    MetaInfoText(
                                        label = "Читав",
                                        value = it.toFormatDateTime(),
                                        fontSize = dateSize
                                    )
                                }
                            }
                        }
                    }

                    // Права панель з кнопками
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Кнопка корзини
                        CartButton(
                            isInCart = isSelected,
                            onClick = onSelect,
                            size = iconSize * 1.8f
                        )

                        // Кнопка відкрити
                        OpenButton(
                            onClick = onOpen,
                            size = iconSize * 1.8f
                        )

                        // Індикатори медіа
                        MediaIndicators(
                            hasImage = word.imageLink != null,
                            hasSound = word.soundLink != null,
                            iconSize = iconSize * 0.9f
                        )
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
private fun CategoryChip(category: String) {
    Box(
        modifier = Modifier
            .background(
                color = WordItemColors.AccentGreen.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = category,
            color = WordItemColors.AccentGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MetaInfoText(
    label: String,
    value: String,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    Column {
        Text(
            text = label,
            color = WordItemColors.TextMuted,
            fontSize = fontSize * 0.9f
        )
        Text(
            text = value,
            color = WordItemColors.TextSecondary.copy(alpha = 0.7f),
            fontSize = fontSize
        )
    }
}

@Composable
private fun CartButton(
    isInCart: Boolean,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isInCart)
            WordItemColors.CartActive.copy(alpha = 0.2f)
        else
            WordItemColors.ButtonBackground,
        animationSpec = tween(200)
    )

    val iconColor by animateColorAsState(
        targetValue = if (isInCart)
            WordItemColors.CartActive
        else
            WordItemColors.TextSecondary,
        animationSpec = tween(200)
    )

    val buttonScale by animateFloatAsState(
        targetValue = if (isInCart) 1.05f else 1f,
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
            if (isInCart) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(size * 0.5f)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "",
                    modifier = Modifier.size(size * 0.5f)
                )
            }
        }
    }
}

@Composable
private fun OpenButton(
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(12.dp),
        color = WordItemColors.AccentCyan.copy(alpha = 0.15f),
        contentColor = WordItemColors.AccentCyan
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = "Відкрити слово",
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}

@Composable
private fun MediaIndicators(
    hasImage: Boolean,
    hasSound: Boolean,
    iconSize: androidx.compose.ui.unit.Dp
) {
    if (!hasImage && !hasSound) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(
                color = WordItemColors.ButtonBackground.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (hasImage) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = "Є зображення",
                tint = WordItemColors.AccentGreen.copy(alpha = 0.8f),
                modifier = Modifier.size(iconSize)
            )
        }

        if (hasSound) {
            Icon(
                imageVector = Icons.Outlined.VolumeUp,
                contentDescription = "Є звук",
                tint = WordItemColors.AccentPurple.copy(alpha = 0.8f),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
