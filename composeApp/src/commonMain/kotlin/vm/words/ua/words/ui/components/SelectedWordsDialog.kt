package vm.words.ua.words.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.TagBadge
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberLabelFontSize
import vm.words.ua.words.domain.models.Word

@Composable
fun SelectedWordsDialog(
    words: List<Word>,
    onDismiss: () -> Unit,
    onDelete: (userWordId: String) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(8.dp)) {
            Column(
                modifier = Modifier
                    .widthIn(min = 280.dp, max = 420.dp)
                    .heightIn(min = 200.dp, max = 560.dp)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Selected Words",
                    color = AppTheme.PrimaryColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = rememberLabelFontSize() * 0.8f,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                if (words.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No words selected",
                            color = AppTheme.SecondaryText,
                            fontSize = rememberFontSize()
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(words, key = { it.id }) { word ->
                            SelectedWordItem(
                                word = word,
                                onDelete = { onDelete(word.id) }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDismiss) {
                        Text("Dismiss", fontSize = rememberLabelFontSize() * 0.7f)
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedWordItem(
    word: Word,
    onDelete: () -> Unit,
) {
    val cardBackground = AppColors.secondaryBack
    val accentPrimary = AppColors.primaryColor
    val fontSize = rememberLabelFontSize()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .border(
                width = 5.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            ),

        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground.compositeOver(Color.Black.copy(alpha = 0.5f))
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TagBadge(
                text = word.lang.upperShortName,
                color = AppTheme.SecondaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = word.original,
                    color = AppColors.primaryText,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = fontSize * 1.3f
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Unselect word",
                    tint = accentPrimary
                )
            }
        }
    }
}