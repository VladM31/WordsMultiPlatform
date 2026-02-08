package vm.words.ua.words.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.domain.models.enums.CEFR
import vm.words.ua.core.platform.AppPlatform
import vm.words.ua.core.platform.currentOrientation
import vm.words.ua.core.platform.currentPlatform
import vm.words.ua.core.platform.isLandscape
import vm.words.ua.core.ui.AppTheme
import vm.words.ua.core.ui.components.AppToolBar
import vm.words.ua.core.ui.components.ErrorMessageBox
import vm.words.ua.core.ui.components.ImageFromBytes
import vm.words.ua.core.utils.*
import vm.words.ua.di.rememberInstance
import vm.words.ua.navigation.SimpleNavController
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word
import vm.words.ua.words.ui.actions.UserWordsAction
import vm.words.ua.words.ui.actions.WordDetailsAction
import vm.words.ua.words.ui.bundles.WordDetailsBundle
import vm.words.ua.words.ui.states.WordDetailsState
import vm.words.ua.words.ui.vms.WordDetailsViewModel
import wordsmultiplatform.composeapp.generated.resources.Res
import wordsmultiplatform.composeapp.generated.resources.delete_red
import wordsmultiplatform.composeapp.generated.resources.image_icon

@Composable
fun WordDetailsScreen(
    navController: SimpleNavController,
    modifier: Modifier = Modifier
) {
    val bundle = navController.getParam<WordDetailsBundle>() ?: throw IllegalStateException()
    val userWord: UserWord? = bundle.userWord
    val word: Word = bundle.word

    val viewModel = rememberInstance<WordDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            navController.popBackStack(UserWordsAction.ReFetch)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sent(WordDetailsAction.Init(userWord, word))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.PrimaryBack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppToolBar(
                title = state.word?.original ?: "Word Details",
                showBackButton = true,
                showAdditionalButton = userWord != null,
                onAdditionalClick = { viewModel.sent(WordDetailsAction.Delete) },
                onBackClick = {
                    navController.popBackStack()
                },
                additionalButtonImage = painterResource(Res.drawable.delete_red)
            )

            if (state.isLoading) {
                LoadingContent()
                return@Column
            }
            // Content
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val maxWidth = maxWidth
                val isPhone = getWidthDeviceFormat(maxWidth).isPhone
                val isNotMobile = remember {
                    setOf(AppPlatform.IOS, AppPlatform.ANDROID).contains(currentPlatform()).not()
                }
                val isNotLandscape = remember { currentOrientation().isLandscape.not() }

                if (isPhone && (isNotLandscape || isNotMobile)) {
                    MobileLayout(
                        word = word,
                        userWord = userWord,
                        wordState = state,
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
        }

        // Error Snackbar
        state.errorMessage?.let { error ->
            ErrorMessageBox(error)
        }
    }
}


@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = AppTheme.PrimaryColor,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Loading...",
                color = AppTheme.SecondaryText,
                fontSize = 14.sp
            )
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val scale = getScaleFactor(maxWidth)
        val cardWidth = (320 * scale).dp

        WordCard(
            word = word,
            modifier = Modifier.widthIn(max = cardWidth)
        )

        if (image != null) {
            ImageCard(
                image = image,
                modifier = Modifier.widthIn(max = cardWidth)
            )
        }

        if (wordState?.sound != null) {
            SoundButton(
                isPlaying = state.isPlayingSound,
                hasSound = true,
                onPlaySound = { viewModel.sent(WordDetailsAction.PlaySound) }
            )
        }

        userWord?.let {
            StatsCard(
                userWord = it,
                modifier = Modifier.widthIn(max = cardWidth)
            )
        }
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
            .padding(24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        val scale = getScaleFactor(maxWidth)
        val contentWidth = (400 * scale).dp

        Column(
            modifier = Modifier
                .widthIn(min = 300.dp, max = contentWidth)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WordCard(
                word = word,
                modifier = Modifier.fillMaxWidth()
            )

            userWord?.let {
                StatsCard(
                    userWord = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                SoundButton(
                    isPlaying = state.isPlayingSound,
                    hasSound = wordState.sound != null,
                    onPlaySound = { viewModel.sent(WordDetailsAction.PlaySound) }
                )
            }


        }

        if (image == null) {
            return@Row
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Right column - Image and Sound (centered when no image)
        Box(
            modifier = Modifier
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ImageCard(
                    image = image
                )
            }
        }
    }
}


@Composable
private fun WordCard(
    word: Word,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.SecondaryBack,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Original Word
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LanguageBadge(langCode = word.lang.upperShortName)
                Text(
                    text = word.original,
                    color = AppTheme.PrimaryColor,
                    fontSize = rememberFontSize() * 1.1,
                    lineHeight = rememberFontSize() * 1.2,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                color = AppTheme.PrimaryColor.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            // Translation
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LanguageBadge(
                    langCode = word.translateLang.upperShortName,
                    backgroundColor = AppTheme.SecondaryColor
                )
                Text(
                    text = word.translate,
                    color = AppTheme.PrimaryText,
                    fontSize = rememberFontSize() * 0.8,
                    lineHeight = rememberFontSize() * 0.9,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Info Chips Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoChip(
                    label = "CEFR",
                    value = word.cefr.name,
                    color = getCefrColor(word.cefr)
                )

                word.category?.let { category ->
                    InfoChip(
                        label = "Category",
                        value = category,
                        color = AppTheme.PrimaryColor
                    )
                }
            }

            // Description
            word.description?.let { description ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = AppTheme.PrimaryBack
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Definition",
                            color = AppTheme.SecondaryText,
                            fontSize = rememberLabelFontSize() * 0.9,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            color = AppTheme.PrimaryText,
                            fontSize = rememberLabelFontSize(),
                            lineHeight = rememberLabelFontSize() * 1.1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageBadge(
    langCode: String,
    backgroundColor: Color = AppTheme.PrimaryColor
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor.copy(alpha = 0.2f)
    ) {
        Text(
            text = langCode,
            color = backgroundColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                color = AppTheme.SecondaryText,
                fontSize = 11.sp
            )
            Text(
                text = value,
                color = color,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun getCefrColor(cefr: CEFR): Color {
    return when (cefr) {
        CEFR.A1 -> AppTheme.PrimaryGreen
        CEFR.A2 -> AppTheme.PrimaryGreenStatus
        CEFR.B1 -> AppTheme.PrimaryBlue
        CEFR.B2 -> AppTheme.PrimaryViolet
        CEFR.C1 -> AppTheme.PrimaryYellow
        CEFR.C2 -> AppTheme.PrimaryRed
    }
}

@Composable
private fun ImageCard(
    image: ByteContent,
    modifier: Modifier = Modifier
) {


    val scale = rememberScaleFactor()
    val imageSize = (300 * scale).dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ImageFromBytes(
            imageBytes = image.bytes,
            defaultPaint = painterResource(Res.drawable.image_icon),
            width = imageSize,
            height = imageSize,
            contentDescription = "Word Image"
        )
    }


}

@Composable
private fun SoundButton(
    isPlaying: Boolean,
    hasSound: Boolean,
    onPlaySound: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 0.95f else 1f,
        animationSpec = tween(100)
    )

    val buttonColor by animateColorAsState(
        targetValue = if (isPlaying) AppTheme.PrimaryColorDark else AppTheme.PrimaryColor,
        animationSpec = tween(200)
    )

    Surface(
        onClick = { if (!isPlaying && hasSound) onPlaySound() },
        modifier = Modifier
            .size(80.dp)
            .scale(scale),
        shape = CircleShape,
        color = if (hasSound) buttonColor else AppTheme.PrimaryGray,
        shadowElevation = if (hasSound) 8.dp else 2.dp,
        enabled = hasSound && !isPlaying
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isPlaying) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.PlayArrow,
                contentDescription = if (isPlaying) "Playing sound" else "Play sound",
                tint = AppTheme.PrimaryBack,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
private fun StatsCard(
    userWord: UserWord,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.SecondaryBack,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Learning Progress",
                color = AppTheme.PrimaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            HorizontalDivider(
                color = AppTheme.PrimaryGray.copy(alpha = 0.3f),
                thickness = 1.dp
            )



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(
                    label = "Created",
                    value = formatInstant(userWord.createdAt),
                    icon = "📅"
                )
                StatItem(
                    label = "Last Read",
                    value = formatInstant(userWord.lastReadDate),
                    icon = "📖"
                )
            }
        }
    }
}

private fun formatInstant(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.dayOfMonth.toString().padStart(2, '0')}." +
            "${localDateTime.monthNumber.toString().padStart(2, '0')}." +
            "${localDateTime.year}"
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = AppTheme.SecondaryText,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = AppTheme.PrimaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}