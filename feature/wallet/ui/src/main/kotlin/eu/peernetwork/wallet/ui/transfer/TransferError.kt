package eu.peernetwork.wallet.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.luna.designTertiaryButtonColors
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed
import eu.peernetwork.feature.wallet.ui.R

@Composable
fun TransferError(
    error: State<Throwable?>,
    component: Transfer.Component,
    modifier: Modifier = Modifier,
    onReset: () -> Unit,
    onRetry: () -> Unit
) {
    val showSheet = remember { derivedStateOf { error.value != null } }
    val isVisible = remember(showSheet.value) { mutableStateOf(showSheet.value) }
    val handleReset by rememberUpdatedState(onReset)
    DesignBottomSheetScaffold(
        state = isVisible,
        dismissable = true,
        onDismiss = {
            isVisible.value = false
            handleReset()
        }
    ) {
        val streamState = remember { derivedStateOf {
            if (error.value == null) {
                DesignStreamState.Default
            } else {
                DesignStreamState.Success(error.value)
            }
        } }
        DesignStream(streamState) { stream ->
            val message = stream.value?.message?.let {
                component.resource().string(it)
            } ?: stringResource(R.string.error_message)
            TransferError(
                message = message,
                modifier = modifier,
                onCancel = { isVisible.value = false },
                onRetry = onRetry
            )
        }
    }
}

@Composable
fun TransferError(
    message: String,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onRetry: () -> Unit
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
                .background(PeerAppDarkRed.copy(alpha = .1f))
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_report),
                contentDescription = stringResource(R.string.error_label),
                tint = PeerAppDarkRed,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
        Text(
            text = stringResource(R.string.error_label),
            color = PeerAppDarkRed,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(bottom = 6.dp)
        ) {
            DesignButton(
                onClick = onCancel,
                minHeight = 48.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designTertiaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_label)) }
            DesignButton(
                onClick = onRetry,
                minHeight = 48.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.retry_again_label)) }
        }
    }
}

@Preview
@Composable
fun PreviewTransferError() {
    DesignTheme(isDarkMode = true) {
        TransferError(
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            modifier = Modifier.padding(20.dp),
            onCancel = {}
        ) {}
    }
}
