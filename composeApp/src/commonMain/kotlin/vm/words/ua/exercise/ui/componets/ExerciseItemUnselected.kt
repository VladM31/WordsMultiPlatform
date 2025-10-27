package vm.words.ua.exercise.ui.componets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.exercise.domain.models.data.ExerciseSelection

@Composable
fun ExerciseItemUnselected(
    exerciseSelection: ExerciseSelection,
    onSelect: () -> Unit
) {
    // Анимация цвета
    val borderColor by animateColorAsState(
        targetValue = AppTheme.PrimaryColor,
        animationSpec = tween(durationMillis = 300),
        label = "borderColor"
    )

    // Анимация толщины бордера
    val borderWidth by animateDpAsState(
        targetValue = 2.dp,
        animationSpec = tween(durationMillis = 300),
        label = "borderWidth"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .padding(vertical = 14.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = exerciseSelection.exercise.text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.PrimaryColor
        )
    }
}
