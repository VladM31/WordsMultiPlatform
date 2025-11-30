package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.*
import vm.words.ua.playlist.domain.models.PlayListCount

@Composable
fun PlayListItem(
    playList: PlayListCount,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val enabled = playList.count > 0L
    val dateText = playList.createdAt.toFormatDateTime()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scaleFactor = getScaleFactor(maxWidth)

        val titleSize = rememberFontSize(scaleFactor)
        val detailsSize = (14 * scaleFactor).sp
        val iconSize = rememberIconSize(scaleFactor)
        val buttonSize = getIconButtonSize(scaleFactor)
        val cardPadding = (16 * scaleFactor).dp
        val horizontalPadding = (16 * scaleFactor).dp
        val verticalPadding = (8 * scaleFactor).dp
        val spacerHeight = (8 * scaleFactor).dp

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.SecondaryBack
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding)
            ) {
                Text(
                    text = playList.name,
                    fontSize = titleSize,
                    color = AppTheme.PrimaryGreen,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(spacerHeight))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Created at: $dateText\nCount of words: ${playList.count}",
                        fontSize = detailsSize,
                        color = AppTheme.PrimaryGreen,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (enabled) {
                                onClick(playList.id)
                            }
                        },
                        enabled = enabled,
                        modifier = Modifier.size(buttonSize)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open words",
                            tint = if (enabled) AppTheme.PrimaryGreen else AppTheme.PrimaryGreen.copy(alpha = 0.3f),
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
        }
    }
}
