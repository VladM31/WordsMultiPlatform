package vm.words.ua.words.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .widthIn(min = 280.dp, max = 420.dp)
                    .heightIn(min = 200.dp, max = 560.dp)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        Text("Dismiss")
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

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TagBadge(
            text = word.lang.upperShortName,
            color = AppTheme.SecondaryColor
        )
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = word.original,
            color = AppTheme.PrimaryText,
            fontSize = rememberLabelFontSize(),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Unselect word",
                tint = AppTheme.PrimaryColor
            )
        }
    }
}