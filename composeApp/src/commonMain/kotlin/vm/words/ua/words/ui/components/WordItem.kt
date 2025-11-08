package vm.words.ua.words.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
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

@Composable
fun WordItem(
    word: Word,
    userWord: UserWord? = null,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val scaleFactor = getScaleFactor(maxWidth)

        val titleSize = (20 * scaleFactor).sp
        val categorySize = (16 * scaleFactor).sp
        val dateSize = (14 * scaleFactor).sp
        val iconSize = (24 * scaleFactor).dp
        val cardPadding = (10 * scaleFactor).dp
        val horizontalPadding = (5 * scaleFactor).dp
        val verticalPadding = (5 * scaleFactor).dp

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { onSelect() }
                    )
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.SecondaryBack
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = "${word.lang.upperShortName}: ${word.original}",
                        color = AppTheme.PrimaryColor,
                        fontSize = titleSize
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${word.translateLang.upperShortName}: ${word.translate}",
                        color = AppTheme.PrimaryColor,
                        fontSize = titleSize
                    )

                    word.category?.let { category ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Category: $category",
                            color = AppTheme.PrimaryColor,
                            fontSize = categorySize
                        )
                    }

                    userWord?.createdAt?.let {
                        Text(
                            text = "Added at: ${it.toFormatDateTime()}",
                            color = AppTheme.PrimaryColor,
                            fontSize = dateSize
                        )
                    }

                    userWord?.lastReadDate?.let {
                        Text(
                            text = "Last read: ${it.toFormatDateTime()}",
                            color = AppTheme.PrimaryColor,
                            fontSize = dateSize
                        )
                    }

                }

                // Right side icons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Check mark
                    if (isSelected) {
                        Icon(
                            painter = painterResource(Res.drawable.check_mark),
                            contentDescription = "Selected",
                            tint = AppTheme.PrimaryColor,
                            modifier = Modifier.size(iconSize)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(iconSize))
                    }

                    // Open button
                    IconButton(
                        onClick = onOpen,
                        modifier = Modifier.size(iconSize * 2).rotate(180f)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow),
                            contentDescription = "Open word",
                            tint = AppTheme.PrimaryColor,
                            modifier = Modifier
                                .size(iconSize * 1.5f)
                        )
                    }

                    // Has image/sound indicators
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (word.imageLink != null) {
                            Icon(
                                painter = painterResource(Res.drawable.image_icon),
                                contentDescription = "Has image",
                                tint = AppTheme.PrimaryColor,
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        if (word.soundLink != null) {
                            Icon(
                                painter = painterResource(Res.drawable.sound),
                                contentDescription = "Has sound",
                                tint = AppTheme.PrimaryColor,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    }
                }
            }
        }
    }
}
