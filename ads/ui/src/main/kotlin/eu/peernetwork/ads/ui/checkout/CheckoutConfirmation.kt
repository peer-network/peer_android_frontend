package eu.peernetwork.ads.ui.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.ads.ui.model.UiOrder
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignDialog
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppLightGreen

@Composable
fun CheckoutConfirmation(
    state: State<UiOrder?>,
    showDialog: State<Boolean>,
    onConfirm: () -> Unit,
    onProfile: () -> Unit,
    onDismiss: () -> Unit,
) {
    val handleConfirm by rememberUpdatedState(onConfirm)
    val streamState = remember { derivedStateOf {
        state.value?.let {
            DesignStreamState.Success(it)
        } ?: DesignStreamState.Default
    } }
    DesignDialog(
        state = showDialog,
        dim = true,
        onDismiss = onDismiss
    ) { controller, progress, dialogState ->
        DesignStream(streamState) { target ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
                    .graphicsLayer {
                        alpha = progress.value
                    }
            ) {
                CheckoutConfirmation(
                    period = target.value.start,
                    duration = stringResource(R.string.advert_duration_label, target.value.duration),
                    onProfile = onProfile
                ) { handleConfirm() }
            }
        }
    }
}

@Composable
fun CheckoutConfirmation(
    period: String,
    duration: String,
    onProfile: () -> Unit,
    onPreview: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_successful),
            contentDescription = stringResource(R.string.boost_label),
        )
        Text(
            text = stringResource(R.string.promotion_start_label),
            color = PeerAppLightGreen,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.promotion_start_description))
                append("\n")
                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )) { append(period) }
                append(" ")
                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.onBackground
                )) { append(duration) }
            },
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            DesignOutlineButton(
                onClick = onProfile,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.visit_profile)) }
            DesignButton(
                onClick = onPreview,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.visit_post)) }
        }
    }
}

@Preview
@Composable
fun PreviewCheckoutConfirmation() {
    DesignTheme(isDarkMode = true) {
        CheckoutConfirmation(
            period = "Jun 25, 2025 at 14:30",
            duration = "(24 hours from now)",
            onProfile = {},
        ) {}
    }
}
