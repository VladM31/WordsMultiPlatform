package vm.words.ua.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.settings.ui.vm.InstructionViewModel
import vm.words.ua.words.ui.components.PdfViewer

@Composable
fun InstructionScreen(
    navController: SimpleNavController,
    viewModel: InstructionViewModel = rememberInstance()
) {
    val state by viewModel.state.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        AppToolBar(
            title = "Instruction",
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            additionalButtonVector = Icons.Outlined.Download,
            showAdditionalButton = true,
            onAdditionalClick = {
                uriHandler.openUri(state.link)
            }
        )

        state.content?.bytes?.let { bytes ->
            PdfViewer(
                pdfData = bytes,
                modifier = Modifier.fillMaxSize(),
                initialPage = 0,
                onError = { error ->
                    errorMessage = error
                    println("PDF Error: $error")
                }
            )
        }

        state.errorMessage?.message?.let {
            errorMessage = it
        }

        errorMessage?.let {
            ErrorMessageBox(ErrorMessage(it))
        }
    }
}