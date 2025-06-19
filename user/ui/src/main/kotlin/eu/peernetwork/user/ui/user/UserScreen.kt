package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignLead
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.Overview
import eu.peernetwork.user.ui.compose.ProfileScaffold
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiOverview
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.DialogProperties
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    id: String,
    lastUpdated: State<Long>,
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    onFollow: @Composable (Pair<Boolean, Boolean>) -> Unit,
    onClick: (Int) -> Unit,
    onSettings: () -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(User.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember(state) {
        derivedStateOf {
            when (state) {
                UserViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                UserViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is UserViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as UserViewModel.State.Success).let {
                            Pair(it.account, it.configurable)
                        }
                    )
                }

                is UserViewModel.State.Error -> {
                    DesignStatefulScaffoldState.Error(
                        (state as UserViewModel.State.Error).error
                    )
                }
            }
        }
    }
    val updatedAt = remember { mutableLongStateOf(lastUpdated.value) }
    DesignStatefulScaffold<Pair<UiAccount, Boolean>>(
        state = derivedState,
        onRefresh = { viewModel.getAccount(id) },
        placeholder = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) },
        errorContent = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) }
    ) {
        UserScreen(
            modifier = modifier,
            account = it.first,
            connection = onFollow,
            showPeers = it.second,
            onSettings = if (it.second) {
                onSettings
            } else {
                null
            },
            onClick = onClick,
        )
    }
    LaunchedEffect(lastUpdated.value) {
        if (updatedAt.longValue != lastUpdated.value) {
            viewModel.getAccount(id)
            updatedAt.longValue = lastUpdated.value
        }
    }
    LaunchedEffect(Unit) { viewModel.initialize() }
}

@Composable
fun UserScreen(
    account: UiAccount,
    modifier: Modifier = Modifier,
    showPeers: Boolean,
    connection: @Composable (Pair<Boolean, Boolean>) -> Unit,
    onSettings: (() -> Unit)? = null,
    onClick: (Int) -> Unit,
) {
    val clickHandler by rememberUpdatedState(onClick)
    val settingsHandler by rememberUpdatedState(onSettings)
    val updatedConnection by rememberUpdatedState(connection)

    val showImageDialog = remember { mutableStateOf(false) }

    ProfileScaffold(
        modifier = modifier,
        avatar = {
            val isImageLoaded = remember { mutableStateOf(false) }

            val avatarModifier = if (isImageLoaded.value) {
                Modifier.clickable { showImageDialog.value = true }
            } else {
                Modifier
            }

            DesignAsyncImage(
                label = account.username,
                imageUrl = account.imageUrl,
                modifier = avatarModifier,
                onImageLoaded = { loaded ->
                    isImageLoaded.value = loaded
                }
            )
        },
        actions = {
            if (settingsHandler != null) {
                IconButton(onClick = { settingsHandler?.invoke() }) {
                    Icon(
                        painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_settings),
                        contentDescription = stringResource(eu.peernetwork.core.ui.R.string.settings_label),
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(bottom = 4.dp)
                ) {
                    updatedConnection(account.isfollowing to account.isfollowed)
                }
            }
        },
        options = {
            Overview(
                overview = account.overview,
                modifier = Modifier.fillMaxWidth(),
                onClick = { if (it < (2 + showPeers.toInt())) clickHandler(it) }
            )
        }
    ) {
        DesignLead(
            account.username,
            account.slug.toString(),
            account.bio ?: stringResource(R.string.empty_description_message)

        )
    }

    if (showImageDialog.value) {
        FullScreenImageDialogDarkBlur(
            imageUrl = account.imageUrl,
            onDismiss = { showImageDialog.value = false }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Suppress("DEPRECATION")

@Composable
fun FullScreenImageDialogDarkBlur(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var shouldDismiss by remember { mutableStateOf(false) }

    val loadFailed = remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        visible = true
        systemUiController.isNavigationBarVisible = false
        systemUiController.isStatusBarVisible = true
    }


    LaunchedEffect(loadFailed.value) {
        if (loadFailed.value) {
            visible = false
            systemUiController.isSystemBarsVisible = true
            onDismiss()
        }
    }

    LaunchedEffect(shouldDismiss) {
        if (shouldDismiss) {
            visible = false
            kotlinx.coroutines.delay(300)
            systemUiController.isSystemBarsVisible = true
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = { shouldDismiss = true },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = scaleIn(
                    initialScale = 0.7f,
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                ),
                exit = scaleOut(
                    targetScale = 0.7f,
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(200.dp),
                        onError = {
                            loadFailed.value = true
                        },
                        onSuccess = {
                            loadFailed.value = false
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.75f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { shouldDismiss = true }
                    )
                    val offsetX = remember { mutableFloatStateOf(0f) }
                    val offsetY = remember { mutableFloatStateOf(0f) }
                    val scale = remember { Animatable(1f) }
                    val scope = rememberCoroutineScope()

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .offset {
                                    if (scale.value == 1f) {
                                        IntOffset(offsetX.floatValue.toInt(), offsetY.floatValue.toInt())
                                    } else {
                                        IntOffset(0, 0)
                                    }
                                }
                                .graphicsLayer {
                                    scaleX = scale.value
                                    scaleY = scale.value
                                    shadowElevation = 19.dp.toPx()
                                    shape = CircleShape
                                    clip = true
                                }
                                .pointerInput(Unit) {
                                    forEachGesture {
                                        awaitPointerEventScope {
                                            var zooming = false
                                            do {
                                                val event = awaitPointerEvent()
                                                val zoomChange = event.calculateZoom()
                                                val pan = event.calculatePan()

                                                if (zoomChange != 1f) zooming = true

                                                val newScale = (scale.value * zoomChange).coerceIn(1f, 2f)
                                                scope.launch {
                                                    scale.snapTo(newScale)
                                                }
                                                if (scale.value == 1f) {
                                                    offsetX.floatValue += pan.x * 0.6f
                                                    offsetY.floatValue += pan.y * 0.6f
                                                }
                                            } while (event.changes.any { it.pressed })

                                            if (zooming) {
                                                scope.launch {
                                                    scale.animateTo(
                                                        1f,
                                                        animationSpec = tween(durationMillis = 350)
                                                    )
                                                }
                                                scope.launch {
                                                    animate(
                                                        initialValue = offsetX.floatValue,
                                                        targetValue = 0f,
                                                        animationSpec = tween(350)
                                                    ) { value, _ -> offsetX.floatValue = value }
                                                }
                                                scope.launch {
                                                    animate(
                                                        initialValue = offsetY.floatValue,
                                                        targetValue = 0f,
                                                        animationSpec = tween(350)
                                                    ) { value, _ -> offsetY.floatValue = value }
                                                }
                                            }
                                        }
                                    }
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            scope.launch {
                                                animate(
                                                    initialValue = offsetX.floatValue,
                                                    targetValue = 0f,
                                                    animationSpec = tween(300)
                                                ) { value, _ -> offsetX.floatValue = value }
                                            }
                                            scope.launch {
                                                animate(
                                                    initialValue = offsetY.floatValue,
                                                    targetValue = 0f,
                                                    animationSpec = tween(300)
                                                ) { value, _ -> offsetY.floatValue = value }
                                            }
                                        }
                                    ) { change, dragAmount ->
                                        change.consume()

                                        if (scale.value == 1f) {
                                            offsetX.floatValue += dragAmount.x * 0.6f
                                            offsetY.floatValue += dragAmount.y * 0.6f
                                        }
                                    }
                                }
                                .size(240.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserScreen() {
    PeerTheme {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = "Description....",
            imageUrl = "",
            overview = UiOverview(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            ),
            isfollowing = false,
            isfollowed = false
        )
        UserScreen(connection = { }, account = model, showPeers = true) {}
    }
}
