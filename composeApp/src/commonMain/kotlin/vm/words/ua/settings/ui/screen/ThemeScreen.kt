package vm.words.ua.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.theme.AppThemeConfig
import vm.words.ua.core.ui.theme.AppThemes
import vm.words.ua.core.ui.theme.ThemeManager
import vm.words.ua.core.ui.theme.rememberCurrentTheme
import vm.words.ua.core.utils.rememberFontSize
import vm.words.ua.navigation.SimpleNavController

@Composable
fun ThemeScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val currentTheme by rememberCurrentTheme()
    val titleSize = rememberFontSize()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(currentTheme.primaryBack)
    ) {
        AppToolBar(
            title = "Choose Theme",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Dark themes section
            item {
                Text(
                    text = "Dark Themes",
                    color = currentTheme.primaryText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(AppThemes.darkThemes) { theme ->
                ThemeCard(
                    theme = theme,
                    isSelected = currentTheme.id == theme.id,
                    onClick = { ThemeManager.instance.setTheme(theme) }
                )
            }

            // Light themes section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Light Themes",
                    color = currentTheme.primaryText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(AppThemes.lightThemes) { theme ->
                ThemeCard(
                    theme = theme,
                    isSelected = currentTheme.id == theme.id,
                    onClick = { ThemeManager.instance.setTheme(theme) }
                )
            }

            // Special themes section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Special Themes",
                    color = currentTheme.primaryText,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(listOf(AppThemes.NeonCyberpunk, AppThemes.RetroSepia)) { theme ->
                ThemeCard(
                    theme = theme,
                    isSelected = currentTheme.id == theme.id,
                    onClick = { ThemeManager.instance.setTheme(theme) }
                )
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: AppThemeConfig,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val currentTheme by rememberCurrentTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = currentTheme.primaryColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = currentTheme.secondaryBack
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme preview colors
            ThemePreview(theme = theme)

            // Theme name and type
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = theme.name,
                    color = currentTheme.primaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (theme.isDark) "Dark" else "Light",
                    color = currentTheme.secondaryText,
                    fontSize = 12.sp
                )
            }

            // Selected checkmark
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = currentTheme.primaryColor,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = if (theme.isDark) Color.Black else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemePreview(theme: AppThemeConfig) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Primary color circle
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = theme.primaryColor,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )
        // Secondary color circle
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = theme.secondaryColor,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )
        // Background preview
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = theme.primaryBack,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )
    }
}

