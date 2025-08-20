package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import eu.peernetwork.wallet.ui.model.UiTransfer
import java.math.BigDecimal
import java.util.UUID
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransferSheet(
    transfer: MutableState<UiTransfer?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    recipient: UiRecipient,
    onFinish: () -> Unit = {},
    onRecipientClick: (UiRecipient) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Transfer.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TransferViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                TransferViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                TransferViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is TransferViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as TransferViewModel.State.Success).transfer
                    )
                }
                is TransferViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as TransferViewModel.State.Error).error
                )
            }
        }
    }
    val isLoading = remember { derivedStateOf { state is TransferViewModel.State.Loading } }
    val isSuccessful = remember { derivedStateOf { derivedState.value is DesignStatefulScaffoldState.Success<*> } }
    val error = remember { derivedStateOf {
        (derivedState.value as? DesignStatefulScaffoldState.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    }}
    val showRecipient = remember { mutableStateOf(false) }
    val showSheet = remember(transfer.value) { mutableStateOf(transfer.value != null) }
    val handleOnFinish by rememberUpdatedState(onFinish)
    val handleOnRecipientClick by rememberUpdatedState(onRecipientClick)
    DesignBottomSheetScaffold(
        state = showSheet,
        onDismiss = {
            if (isSuccessful.value) {
                viewModel.reset()
                handleOnFinish()
            } else if (showRecipient.value) {
                showRecipient.value = false
                handleOnRecipientClick(recipient)
            }
            transfer.value = null
        },
    ) {
        Crossfade(transfer.value) { target ->
            if (target != null) {
                TransferSheet(
                    isLoading,
                    error,
                    isSuccessful,
                    recipient,
                    target.token,
                    {
                        showSheet.value = false
                        showRecipient.value = true }
                ) {
                    if (isSuccessful.value) {
                        showSheet.value = false
                    } else {
                        viewModel.transfer(target.recipient, target.token)
                    }
                }
            }
        }
    }
}

@Composable
fun TransferSheet(
    state: State<Boolean>,
    error: State<String?>,
    isSuccessful: State<Boolean>,
    recipient: UiRecipient,
    token: BigDecimal,
    onClick: () -> Unit = {},
    onSubmit: () -> Unit
) {
    Crossfade(isSuccessful.value) { target ->
        if (target) {
            Box {
            TransferSheetScaffold(
                state = state,
                error = error,
                title = stringResource(R.string.sent_label),
                recipient = recipient,
                token = token,
                action = stringResource(R.string.close_label),
                onClick = onClick,
                onSubmit = onSubmit,
            ) {
                LottieTickIcon(
                    playKey = true,
                    modifier = Modifier.size(36.dp),
                    rawRes = R.raw.tick
                )
            }
                TransferSuccessOverlay(
                    playKey = true,
                    rawRes = R.raw.tick
                )}
        } else {
            TransferSheetScaffold(
                state = state,
                error = error,
                title = stringResource(R.string.recipient_label),
                recipient = recipient,
                token = token,
                action = stringResource(R.string.send_label),
                onClick = onClick,
                onSubmit = onSubmit,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_transfer),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.surfaceDim
                )
            }
        }
    }
}

@Composable
private fun LottieTickIcon(
    playKey: Boolean,
    modifier: Modifier = Modifier,
    rawRes: Int = R.raw.tick
) {
    var playing by remember(playKey) { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = playing,
        iterations = 1,
        speed = 1f
    )
    LaunchedEffect(playKey) {
        if (playKey) {
            playing = false
            playing = true
        }
    }
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

@Composable
private fun BoxScope.TransferSuccessOverlay(
    playKey: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 320.dp,
    rawRes: Int = R.raw.peerrocket,
    fillToWidthEdges: Boolean = true,
    overscanScale: Float = 1.12f,
    anchorBottom: Boolean = true
) {
    var play by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = play,
        iterations = 1,
        speed = 1f
    )

    LaunchedEffect(playKey) {
        if (playKey) {
            play = false
            play = true
        }
    }
    LaunchedEffect(progress) {
        if (progress >= 1f) play = false
    }

    val aspect: Float = remember(composition) {
        val b = composition?.bounds
        val w = b?.width()?.toFloat() ?: 1f
        val h = b?.height()?.toFloat() ?: 1f
        if (h > 0f) w / h else 1f
    }

    AnimatedVisibility(
        visible = play && progress < 1f,
        enter = fadeIn(animationSpec = tween(160)),
        exit = fadeOut(animationSpec = tween(220)),
        modifier = modifier.matchParentSize()
    ) {
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = if (anchorBottom) Alignment.BottomCenter else Alignment.Center
        ) {
            val lottieMod = if (fillToWidthEdges) {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspect, matchHeightConstraintsFirst = false)
                    .graphicsLayer {
                        scaleX = overscanScale
                        scaleY = overscanScale
                    }
            } else {
                Modifier.size(size)
            }

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = lottieMod
            )
        }
    }
}
@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferSheet() {
    PeerTheme {
        Column {
            val recipient = UiRecipient(
                id = UUID.randomUUID().toString(),
                slug = "1234",
                username = "johnDoe",
                imageUrl = "http://localhost"
            )
            TransferSheet(
                state = remember { mutableStateOf(true) },
                error = remember { mutableStateOf(null) },
                isSuccessful = remember { mutableStateOf(false) },
                recipient = recipient,
                token = BigDecimal(1.0),
                {}
            ) {}
            Spacer(modifier = Modifier.height(16.dp))
            TransferSheet(
                state = remember { mutableStateOf(false) },
                error = remember { mutableStateOf("Error message...") },
                isSuccessful = remember { mutableStateOf(false) },
                recipient = recipient,
                token = BigDecimal(1.0),
                {}
            ) {}
            Spacer(modifier = Modifier.height(16.dp))
            TransferSheet(
                state = remember { mutableStateOf(false) },
                error = remember { mutableStateOf(null) },
                isSuccessful = remember { mutableStateOf(true) },
                recipient = recipient,
                token = BigDecimal(1.0),
                {}
            ) {}
        }
    }
}
