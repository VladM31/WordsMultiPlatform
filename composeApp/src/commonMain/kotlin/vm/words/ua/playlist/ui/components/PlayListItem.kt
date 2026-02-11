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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.utils.*
import vm.words.ua.playlist.domain.models.PlayListCountable

@Composable
fun PlayListItem(
    playList: PlayListCountable,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showCreatedDate: Boolean = true
) {
    // Use reactive colors that update when theme changes
    val cardBackground = AppColors.secondaryBack
    val accentPrimary = AppColors.primaryColor
    val textPrimary = AppColors.primaryText
    val textMuted = AppColors.primaryDisable

    val enabled = playList.count > 0L
    val dateText = playList.createdAt.toFormatDateTime()

    val scaleFactor = rememberScaleFactor()

    val titleSize = rememberFontSize()
    val iconSize = rememberIconSize() * 0.8f
    val cardPadding = (16 * scaleFactor).dp
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
                            Spacer(modifier = Modifier.height(6.dp))

                            // Count info
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CountBadge(count = playList.count, accentColor = accentPrimary)
                            }

                            // Date shown below the count
                            if (showCreatedDate) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = dateText,
                                    color = textMuted,
                                    fontSize = rememberLabelFontSize() * 0.9,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }

                        // Right panel with button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OpenButton(
                                onClick = { if (enabled) onClick(playList.id) },
                                enabled = enabled,
                                size = iconSize * 1.4f,
                                accentColor = accentPrimary,
                                disabledColor = textMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CountBadge(count: Long, accentColor: Color) {
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
private fun OpenButton(
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
