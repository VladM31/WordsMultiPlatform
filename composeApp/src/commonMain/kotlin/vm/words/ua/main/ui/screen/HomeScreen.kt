package vm.words.ua.main.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.main.ui.components.BottomNavBar
import vm.words.ua.navigation.SimpleNavController

@Composable
fun HomeScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(title = "Menu", showBackButton = false)

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.PrimaryColor,
                contentColor = AppTheme.PrimaryBack
            )

            Button(
                onClick = { navController.navigate("words") },
                colors = buttonColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Words")
            }

            Button(
                onClick = { navController.navigate("user_words") },
                colors = buttonColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "My Words")
            }

            Button(
                onClick = { navController.navigate("add_word") },
                colors = buttonColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Add Word")
            }

            Button(
                onClick = { navController.navigate("instruction") },
                colors = buttonColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Instruction")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        BottomNavBar(currentRoute = "home", onNavigate = { route -> navController.navigate(route) })
    }
}
