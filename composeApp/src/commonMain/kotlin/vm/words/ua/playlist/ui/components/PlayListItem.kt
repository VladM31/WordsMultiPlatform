package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.*
import vm.words.ua.playlist.domain.models.PlayListCountable

private object PlayListItemColors {
    val CardBackground = AppTheme.PrimaryBackLight
    val CardBackgroundGradientEnd = AppTheme.PrimaryBackLight
    val AccentPrimary = AppTheme.PrimaryColor
    val TextPrimary = AppTheme.PrimaryText
    val TextMuted = AppTheme.PrimaryDisable
    val ButtonBackground = AppTheme.SecondaryBack
}

@Composable
fun PlayListItem(
    playList: PlayListCountable,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showCreatedDate: Boolean = true
) {
    val enabled = playList.count > 0L
    val dateText = playList.createdAt.toFormatDateTime()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scaleFactor = getScaleFactor(maxWidth)

        val titleSize = rememberFontSize()
        val iconSize = rememberIconSize() * 0.8f
        val cardPadding = (16 * scaleFactor).dp
        val horizontalPadding = (8 * scaleFactor).dp
        val verticalPadding = (6 * scaleFactor).dp

        // Check if we have tags or CEFRs to display
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
                                PlayListItemColors.CardBackground,
                                PlayListItemColors.CardBackgroundGradientEnd
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
                        // Playlist icon
                        Box(
                            modifier = Modifier
                                .background(
                                    color = PlayListItemColors.AccentPrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.PlaylistPlay,
                                contentDescription = null,
                                tint = PlayListItemColors.AccentPrimary,
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
                                color = PlayListItemColors.TextPrimary,
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
                                    LanguageBadge(
                                        langCode = lang.upperShortName,
                                        color = PlayListItemColors.AccentPrimary
                                    )
                                }
                                playList.translateLanguage?.let { lang ->
                                    LanguageBadge(
                                        langCode = lang.upperShortName,
                                        color = AppTheme.SecondaryColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Count and date info
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CountBadge(count = playList.count)
                                if (showCreatedDate) {
                                    Text(
                                        text = dateText,
                                        color = PlayListItemColors.TextMuted,
                                        fontSize = rememberLabelFontSize() * 0.9
                                    )
                                }
                            }
                        }

                        // Right panel with button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Open button
                            OpenButton(
                                onClick = { if (enabled) onClick(playList.id) },
                                enabled = enabled,
                                size = iconSize * 1.4f
                            )
                        }
                    }

                    if (hasCefrs.not() && hasTags.not()) {
                        return@Column
                    }

                    // Tags section

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = cardPadding)
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (hasCefrs) {
                            Text(
                                "CEFR:",
                                fontSize = rememberLabelFontSize() * 0.9,
                                color = PlayListItemColors.TextMuted
                            )
                            playList.cefrs?.forEach { cefr ->
                                CefrChip(cefr = cefr.name)
                            }
                        }

                        if (hasTags) {
                            Text(
                                "Tags:",
                                fontSize = rememberLabelFontSize() * 0.9,
                                color = PlayListItemColors.TextMuted
                            )
                            playList.tags?.forEach { tag ->
                                TagChip(tag = tag)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

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
            fontSize = rememberLabelFontSize(),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CountBadge(count: Long) {
    Box(
        modifier = Modifier
            .background(
                color = PlayListItemColors.AccentPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$count words",
            color = PlayListItemColors.AccentPrimary,
            fontSize = rememberLabelFontSize(),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CefrChip(cefr: String) {
    Box(
        modifier = Modifier
            .background(
                color = AppTheme.SecondaryColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = cefr,
            color = AppTheme.SecondaryColor,
            fontSize = rememberLabelFontSize() * 0.9,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}

@Composable
private fun TagChip(tag: String) {
    Box(
        modifier = Modifier
            .background(
                color = PlayListItemColors.AccentPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = tag,
            color = PlayListItemColors.AccentPrimary,
            fontSize = rememberLabelFontSize() * 0.9,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}


@Composable
private fun OpenButton(
    onClick: () -> Unit,
    enabled: Boolean,
    size: androidx.compose.ui.unit.Dp
) {
    val backgroundColor = if (enabled)
        PlayListItemColors.AccentPrimary.copy(alpha = 0.15f)
    else
        PlayListItemColors.ButtonBackground.copy(alpha = 0.3f)

    val iconColor = if (enabled)
        PlayListItemColors.AccentPrimary
    else
        PlayListItemColors.TextMuted

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(size),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        contentColor = iconColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "Open playlist",
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}
