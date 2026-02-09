package eu.peernetwork.wallet.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppLightGreen
import eu.peernetwork.feature.wallet.ui.R

@Composable
fun TransferSuccess(
    showSheet: State<Boolean>,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
) {
    val handleConfirm by rememberUpdatedState(onConfirm)
    val isVisible = remember(showSheet.value) { mutableStateOf(showSheet.value) }
    DesignBottomSheetScaffold(
        state = isVisible,
        dismissable = false,
        onDismiss = {
            isVisible.value = false
            handleConfirm()
        }
    ) {
        TransferSuccess(
            modifier = modifier,
            onConfirm = { isVisible.value = false }
        )
    }
}

@Composable
fun TransferSuccess(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(PeerAppLightGreen.copy(alpha = .1f))
        ) {
            TransferSuccessIcon(
                Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
        Text(
            text = stringResource(R.string.transaction_message),
            color = PeerAppLightGreen,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
        DesignButton(
            onClick = onConfirm,
            minHeight = 48.dp,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(bottom = 6.dp)
        ) { Text(stringResource(R.string.got_it_label)) }
    }
}

@Composable
fun TransferSuccessIcon(
    modifier: Modifier = Modifier,
    rawRes: Int = R.raw.tick
) {
    var playing by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = playing,
        reverseOnRepeat = true,
        iterations = LottieConstants.IterateForever,
        speed = 2f
    )
    LaunchedEffect(Unit) { playing = true }
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewCheckoutModal() {
    DesignTheme(isDarkMode = true) {
        TransferSuccess(modifier = Modifier.padding(20.dp)) {}
    }
}
