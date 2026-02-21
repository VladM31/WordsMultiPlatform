package vm.words.ua.playlist.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.ui.components.TagBadge
import vm.words.ua.core.ui.theme.toColor
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberIconSize
import vm.words.ua.core.utils.rememberScaleFactor
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.playlist.domain.models.PlayListCountable

@Composable
fun FastStartPlayListItem(
    playList: PlayListCountable,
    isLoading: Boolean,
    isExpanded: Boolean,
    words: List<PlayList.PinnedWord>,
    onStartClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackground = AppColors.secondaryBack
    val accentPrimary = AppColors.primaryColor
    val accentGreen = AppColors.primaryGreen
    val textPrimary = AppColors.primaryText
    val textMuted = AppColors.primaryDisable

    val hasWords = playList.count > 0L

    val scaleFactor = rememberScaleFactor()
    val titleSize = rememberFontSize() * 0.9f
    val iconSize = rememberIconSize() * 0.75f
    val cardPadding = (14 * scaleFactor).dp
    val horizontalPadding = (8 * scaleFactor).dp
    val verticalPadding = (6 * scaleFactor).dp

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.15f),
                    spotColor = Color.Black.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cardPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ContentView(playList, textPrimary, titleSize, accentPrimary, textMuted)

                    // Start + Expand buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FastStartButton(
                            onClick = onStartClick,
                            isLoading = isLoading,
                            enabled = hasWords && !isLoading,
                            size = iconSize,
                            accentColor = accentGreen,
                            disabledColor = textMuted
                        )
                        FastExpandButton(
                            onClick = onExpandClick,
                            isExpanded = isExpanded,
                            enabled = hasWords,
                            size = iconSize,
                            accentColor = accentPrimary,
                            disabledColor = textMuted
                        )
                    }
                }

                // Expanded words list
                AnimatedVisibility(
                    visible = isExpanded && words.isNotEmpty(),
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = cardPadding)
                            .padding(bottom = cardPadding)
                    ) {
                        HorizontalDivider(
                            color = textMuted.copy(alpha = 0.2f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        words.forEach { pinnedWord ->
                            PinnedWordItem(pinnedWord = pinnedWord)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.ContentView(
    playList: PlayListCountable,
    textPrimary: Color,
    titleSize: TextUnit,
    accentPrimary: Color,
    textMuted: Color
) {
    val scaleFactor = rememberScaleFactor()
    val arrowIconSize = (10 * scaleFactor).dp
    val hasLangs = playList.language != null || playList.translateLanguage != null
    val cefrs = playList.cefrs?.sortedBy { it.ordinal }

    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
        Text(
            text = playList.name,
            color = textPrimary,
            fontSize = titleSize,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group 1: word count
            TagBadge("${playList.count} words", accentPrimary)

            // Delimiter 1
            if (hasLangs) {
                MetaDot(textMuted)
            }

            // Group 2: langs (translateLang → lang)
            playList.translateLanguage?.let {
                TagBadge(it.upperShortName, color = AppColors.secondaryColor)
            }
            if (playList.language != null && playList.translateLanguage != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = textMuted,
                    modifier = Modifier.size(arrowIconSize)
                )
            }
            playList.language?.let {
                TagBadge(it.upperShortName, color = accentPrimary)
            }

            // Delimiter 2 + CEFRs
            if (cefrs.isNullOrEmpty().not()) {
                MetaDot(textMuted)
                cefrs.forEach { cefr ->
                    TagBadge(cefr.name, cefr.toColor())
                }
            }
        }
    }
}

@Composable
private fun MetaDot(color: Color) {
    val size = (rememberScaleFactor() * 3).dp
    Box(
        modifier = Modifier
            .size(size)
            .background(color.copy(alpha = 0.4f), CircleShape)
    )
}

@Composable
private fun FastStartButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
    size: Dp,
    accentColor: Color,
    disabledColor: Color
) {
    val backgroundColor = if (enabled) accentColor.copy(alpha = 0.15f) else disabledColor.copy(alpha = 0.15f)
    val iconColor = if (enabled) accentColor else disabledColor

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        contentColor = iconColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(size * 0.45f),
                    color = iconColor,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Start playlist",
                    modifier = Modifier.size(size * 0.55f)
                )
            }
        }
    }
}

@Composable
private fun FastExpandButton(
    onClick: () -> Unit,
    isExpanded: Boolean,
    enabled: Boolean,
    size: Dp,
    accentColor: Color,
    disabledColor: Color
) {
    val backgroundColor = if (enabled) accentColor.copy(alpha = 0.1f) else disabledColor.copy(alpha = 0.1f)
    val iconColor = if (enabled) accentColor else disabledColor

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        contentColor = iconColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}