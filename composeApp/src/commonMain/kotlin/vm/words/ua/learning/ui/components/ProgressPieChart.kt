package vm.words.ua.learning.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.core.utils.rememberLabelFontSize

@Composable
fun ProgressPieChart(
    learned: Int,
    need: Int,
    modifier: Modifier = Modifier
) {
    val learnedPercent = if (need > 0) {
        (learned.toFloat() / need) * 100f
    } else 0f

    // Анимация
    val animatedLearned by animateFloatAsState(
        targetValue = learnedPercent,
        animationSpec = tween(durationMillis = 500),
        label = "pie_animation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 40.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val topLeft = Offset(
                    (size.width - radius * 2) / 2,
                    (size.height - radius * 2) / 2
                )
                val arcSize = Size(radius * 2, radius * 2)

                drawArc(
                    color = Color(0xFFDC0101),
                    startAngle = -90f + animatedLearned * 3.6f,
                    sweepAngle = (100f - animatedLearned) * 3.6f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )

                // Learned (зелёный)
                drawArc(
                    color = Color(0xFF44F2C1),
                    startAngle = -90f,
                    sweepAngle = animatedLearned * 3.6f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
            }

            // Center text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Progress",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberLabelFontSize(),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${animatedLearned.toInt()}%",
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize(),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            horizontalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            LegendItem(color = AppTheme.PrimaryColor, label = "Learned")
            LegendItem(color = AppTheme.PrimaryRed, label = "Not learned")
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Text(
            text = label,
            color = AppTheme.PrimaryColor,
            fontSize = 14.sp
        )
    }
}