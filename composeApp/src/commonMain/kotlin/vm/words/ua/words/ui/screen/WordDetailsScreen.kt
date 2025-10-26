package vm.words.ua.words.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.domain.models.enums.DeviceFormat
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.ImageFromBytes
import vm.words.ua.core.ui.components.PopupMenuButton
import vm.words.ua.core.ui.components.PopupMenuItem
import vm.words.ua.core.utils.getFontSize
import vm.words.ua.core.utils.getScaleFactor
import vm.words.ua.core.utils.getWidthDeviceFormat
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.ui.actions.WordDetailsAction
import vm.words.ua.words.ui.states.WordDetailsState
import vm.words.ua.words.ui.vms.WordDetailsViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete_red
import wordsmultiplatform.composeapp.generated.resources.image_icon
import wordsmultiplatform.composeapp.generated.resources.sound

@Composable
fun WordDetailsScreen(
    userWord: UserWord?,
    word: Word,
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val viewModel = rememberInstance<WordDetailsViewModel>()
    val state by viewModel.state.collectAsState()


    // Fetch word on first composition
    LaunchedEffect(Unit) {
        viewModel.sent(WordDetailsAction.Init(userWord, word))
    }

    // Handle navigation back when word is deleted
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        Box {
            AppToolBar(
                title = state.word?.original ?: "Word Details",
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                showAdditionalButton = false
            )

            // Popup menu positioned at top right
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                PopupMenuButton(
                    items = listOf(
                        PopupMenuItem(
                            text = "Delete",
                            icon = painterResource(Res.drawable.delete_red),
                            onClick = { viewModel.sent(WordDetailsAction.Delete) }
                        )
                    )
                )
            }
        }

        // Content
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppTheme.PrimaryColor)
            }
            return
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxWidth = maxWidth
            val isPhone = getWidthDeviceFormat(maxWidth).isPhone

            if (isPhone) {
                MobileLayout(
                    word = word,
                    userWord = userWord,
                    wordState =  state,
                    image = state.image,
                    maxWidth = maxWidth,
                    state = state,
                    viewModel = viewModel
                )
                return@BoxWithConstraints
            }

            DesktopLayout(
                word = word,
                userWord = userWord,
                wordState = state,
                image = state.image,
                maxWidth = maxWidth,
                state = state,
                viewModel = viewModel
            )
        }



        state.errorMessage?.let {
            ErrorMessageBox(it)
        }
    }
}

@Composable
private fun MobileLayout(
    word: Word,
    userWord: UserWord?,
    wordState: WordDetailsState?,
    image: ByteContent?,
    maxWidth: Dp,
    state: WordDetailsState,
    viewModel: WordDetailsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scale = getScaleFactor(maxWidth)
        val width = (300 * scale).dp

        // Image and Sound
        FileView(
            hasSound = false,
            hideSound = true,
            image = image,
            maxWidth = maxWidth,
            isPlayingSound = state.isPlayingSound,
            onPlaySound = { viewModel.sent(WordDetailsAction.PlaySound) },
            modifier = Modifier
                .widthIn(width)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Text Content
        Column(
            modifier = Modifier
                .widthIn(width)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Top
        ) {
            WordDetailsContent(
                word = word,
                maxWidth = maxWidth,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(24.dp))
            userWord?.let {
                UserWordDetails(userWord = it, maxWidth = maxWidth)
            }
        }

        // Image and Sound
        FileView(
            hasSound = wordState?.sound != null,
            hideImage = true,
            image = null,
            maxWidth = maxWidth,
            isPlayingSound = state.isPlayingSound,
            onPlaySound = { viewModel.sent(WordDetailsAction.PlaySound) },
            modifier = Modifier
                .widthIn(width)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )

    }
}

@Composable
private fun DesktopLayout(
    word: Word,
    userWord: UserWord?,
    wordState: WordDetailsState,
    image: ByteContent?,
    maxWidth: Dp,
    state: WordDetailsState,
    viewModel: WordDetailsViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Left Column - Text Content
        val leftScrollState = rememberScrollState()
        val scale = getScaleFactor(maxWidth)
        val contentWidth = (400 * scale).dp
        val contentHeight = (500 * scale).dp * 1.2f

        Column(
            modifier = Modifier
                .widthIn(min = 300.dp, max = contentWidth)
                .heightIn(max = contentHeight)
                .verticalScroll(leftScrollState)
                .padding(start = 4.dp, end = 4.dp, top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            WordDetailsContent(
                word = word,
                maxWidth = maxWidth,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(24.dp))
            userWord?.let {
                UserWordDetails(userWord = it, maxWidth = maxWidth)
            }
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Right Column - Image and Sound
        FileView(
            hasSound = wordState.sound != null,
            image = image,
            maxWidth = maxWidth,
            isPlayingSound = state.isPlayingSound,
            onPlaySound = { viewModel.sent(WordDetailsAction.PlaySound) },
            modifier = Modifier
                .widthIn(min = 300.dp, max = contentWidth)
                .heightIn(max = contentHeight)
                .padding(start = 4.dp, end = 4.dp, top = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun FileView(
    hasSound: Boolean,

    maxWidth: Dp = Dp.Unspecified,
    image: ByteContent? = null,
    modifier: Modifier = Modifier,
    isPlayingSound: Boolean,
    onPlaySound: () -> Unit,
    hideImage: Boolean = false,
    hideSound: Boolean = false
) {
    val scale = getScaleFactor(maxWidth)
    val imageSize = (300 * scale).dp

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (hideImage.not()){
            ImageFromBytes(
                imageBytes = image?.bytes,
                defaultPaint = painterResource(Res.drawable.image_icon),
                width = imageSize,
                height = imageSize,
                contentDescription = "Word Image"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (hideSound.not()){
            Button(
                onClick = onPlaySound,
                enabled = !isPlayingSound && hasSound,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasSound) AppTheme.PrimaryColor else AppTheme.PrimaryGray
                ),
                modifier = Modifier
                    .size(80.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.sound),
                    contentDescription = "Play sound",
                    tint = AppTheme.PrimaryBack,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
private fun WordDetailsContent(
    word: Word,
    maxWidth: Dp = Dp.Unspecified,
    modifier: Modifier = Modifier
) {
    val scale = getScaleFactor(maxWidth)
    val fontSize = getFontSize(scale)

    Column(
        modifier = modifier
    ) {
        // Category
        if (word.category != null) {
            Text(
                text = "Category: ${word.category}",
                color = AppTheme.PrimaryColor,
                fontSize = fontSize
            )
        }

        // CEFR Level
        Text(
            text = "CEFR: ${word.cefr}",
            color = AppTheme.PrimaryColor,
            fontSize = fontSize
        )

        // Original word
        Text(
            text = "${word.lang.upperShortName}: ${word.original}",
            color = AppTheme.PrimaryColor,
            fontSize = fontSize
        )

        // Translation
        Text(
            text = "${word.translateLang.upperShortName}: ${word.translate}",
            color = AppTheme.PrimaryColor,
            fontSize = fontSize
        )

        // Description
        if (word.description != null) {
            Text(
                text = "Definition: ${word.description}",
                color = AppTheme.PrimaryColor,
                fontSize = fontSize * 0.7f
            )
        }
    }
}

@Composable
private fun UserWordDetails(
    maxWidth: Dp = Dp.Unspecified,
    userWord: UserWord
) {
    val scale = getScaleFactor(maxWidth)
    val fontSize = getFontSize(scale) * 0.7f

    Text(
        text = "Created: ${userWord.createdAt}",
        color = AppTheme.SecondaryText,
        fontSize = fontSize
    )

    Text(
        text = "Last read: ${userWord.lastReadDate}",
        color = AppTheme.SecondaryText,
        fontSize = fontSize
    )
}
