package vm.words.ua.playlist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppColors
import vm.words.ua.core.ui.components.TagBadge
import vm.words.ua.core.ui.theme.toColor
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberScaleFactor
import vm.words.ua.playlist.domain.models.PlayList
import vm.words.ua.words.domain.models.Word

@Composable
fun PinnedWordItem(
    pinnedWord: PlayList.PinnedWord,
    modifier: Modifier = Modifier
) {
    val word = pinnedWord.userWord.word

    val cardBackground = AppColors.primaryBack
    val accentPrimary = AppColors.primaryColor
    val accentSecondary = AppColors.secondaryColor
    val textPrimary = AppColors.primaryText
    val accentViolet = AppColors.primaryViolet

    val scaleFactor = rememberScaleFactor()
    val titleSize = rememberFontSize() * 0.8f
    val innerPadding = (8 * scaleFactor).dp
    val horizontalPadding = (4 * scaleFactor).dp
    val verticalPadding = (4 * scaleFactor).dp

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = Color.Black.copy(alpha = 0.08f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                ),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: original word + translation
                    WordMainBox(word, textPrimary, titleSize, accentPrimary)

                    WordDetails(word)
                }

            }
        }
    }
}

@Composable
private fun RowScope.WordMainBox(
    word: Word,
    textPrimary: Color,
    titleSize: TextUnit,
    accentPrimary: Color
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(end = 6.dp)
    ) {
        Text(
            text = word.original,
            color = textPrimary,
            fontSize = titleSize,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = word.translate,
            color = accentPrimary,
            fontSize = titleSize * 0.9f,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )
    }
}

@Composable
private fun WordDetails(
    word: Word
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        TagBadge(text = word.cefr.name, color = word.cefr.toColor())
    }
}

