package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.utils.*
import vm.words.ua.playlist.domain.models.PlayListCountable

@Composable
fun ExplorePlayListItem(
    playList: PlayListCountable,
    isAssigning: Boolean = false,
    onAssignClick: () -> Unit,
    onViewClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Use reactive colors
    val cardBackground = AppColors.secondaryBack
    val accentPrimary = AppColors.primaryColor
    val accentSecondary = AppColors.secondaryColor
    val textPrimary = AppColors.primaryText
    val textMuted = AppColors.primaryDisable
    val accentGreen = AppColors.primaryGreen

    val enabled = playList.count > 0L

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scaleFactor = getScaleFactor(maxWidth)

        val titleSize = rememberFontSize()
        val iconSize = rememberIconSize() * 0.8f
        val cardPadding = (16 * scaleFactor).dp
        val horizontalPadding = (8 * scaleFactor).dp
        val verticalPadding = (6 * scaleFactor).dp

        val hasCefrs = playList.cefrs?.isNotEmpty() == true
        val hasTags = playList.tags?.isNotEmpty() == true

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
                            colors = listOf(cardBackground, cardBackground)
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
                        // Playlist icon
                        Box(
                            modifier = Modifier
                                .background(
                                    color = accentPrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.PlaylistPlay,
                                contentDescription = null,
                                tint = accentPrimary,
                                modifier = Modifier.size(iconSize * 1.5f)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Main content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        ) {
                            // Playlist name
                            Text(
                                text = playList.name,
                                color = textPrimary,
                                fontSize = titleSize,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Language badges row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                playList.language?.let { lang ->
                                    LanguageBadgeExplore(
                                        langCode = lang.upperShortName,
                                        color = accentPrimary
                                    )
                                }
                                playList.translateLanguage?.let { lang ->
                                    LanguageBadgeExplore(
                                        langCode = lang.upperShortName,
                                        color = accentSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Count info
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CountBadgeExplore(count = playList.count, accentColor = accentPrimary)
                            }
                        }

                        // Right panel with view and assign buttons
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ViewButton(
                                onClick = onViewClick,
                                enabled = enabled,
                                size = iconSize * 1.4f,
                                accentColor = accentPrimary,
                                disabledColor = textMuted
                            )
                            AssignButton(
                                onClick = onAssignClick,
                                enabled = !isAssigning,
                                size = iconSize * 1.4f,
                                accentColor = accentGreen,
                                disabledColor = textMuted
                            )
                        }
                    }

                    if (hasCefrs || hasTags) {
                        // Tags section with FlowRow for wrapping
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = cardPadding)
                                .padding(top = 4.dp, bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (hasCefrs) {
                                Text(
                                    "CEFR:",
                                    fontSize = rememberLabelFontSize() * 0.9,
                                    color = textMuted
                                )
                                playList.cefrs?.forEach { cefr ->
                                    CefrChipExplore(cefr = cefr.name, accentColor = accentSecondary)
                                }
                            }

                            if (hasTags) {
                                Text(
                                    "Tags:",
                                    fontSize = rememberLabelFontSize() * 0.9,
                                    color = textMuted
                                )
                                playList.tags?.forEach { tag ->
                                    TagChipExplore(tag = tag, accentColor = accentPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageBadgeExplore(
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
            fontSize = rememberLabelFontSize(),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CountBadgeExplore(count: Long, accentColor: Color) {
    Box(
        modifier = Modifier
            .background(
                color = accentColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$count words",
            color = accentColor,
            fontSize = rememberLabelFontSize(),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CefrChipExplore(cefr: String, accentColor: Color) {
    Box(
        modifier = Modifier
            .background(
                color = accentColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = cefr,
            color = accentColor,
            fontSize = rememberLabelFontSize() * 0.9,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}

@Composable
private fun TagChipExplore(tag: String, accentColor: Color) {
    Box(
        modifier = Modifier
            .background(
                color = accentColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = tag,
            color = accentColor,
            fontSize = rememberLabelFontSize() * 0.9,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}

@Composable
private fun ViewButton(
    onClick: () -> Unit,
    enabled: Boolean,
    size: Dp,
    accentColor: Color,
    disabledColor: Color
) {
    val backgroundColor = if (enabled)
        accentColor.copy(alpha = 0.15f)
    else
        disabledColor.copy(alpha = 0.3f)

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
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "View playlist",
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}

@Composable
private fun AssignButton(
    onClick: () -> Unit,
    enabled: Boolean,
    size: Dp,
    accentColor: Color,
    disabledColor: Color
) {
    val backgroundColor = if (enabled)
        accentColor.copy(alpha = 0.15f)
    else
        disabledColor.copy(alpha = 0.3f)

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
            if (enabled) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add playlist",
                    modifier = Modifier.size(size * 0.5f)
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(size * 0.4f),
                    color = iconColor,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}
