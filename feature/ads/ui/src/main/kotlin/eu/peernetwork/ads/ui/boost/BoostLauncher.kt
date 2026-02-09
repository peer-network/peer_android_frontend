package eu.peernetwork.ads.ui.boost

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designPrimaryButtonColors
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.luna.designTertiaryButtonColors
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed
import eu.peernetwork.feature.ads.ui.R

@Composable
fun BoostLauncher(
    title: String,
    description: String,
    prompt: String,
    color: Color,
    hasWarning: Boolean,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(16.dp)
    ) {
        updatedContent()
        Text(
            text = title,
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = description,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            DesignButton(
                onClick = onCancel,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designTertiaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_label)) }
            DesignButton(
                onClick = onConfirm,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f),
                colors = if (hasWarning) {
                    designSecondaryButtonColors()
                } else {
                    designPrimaryButtonColors()
                }
            ) { Text(prompt) }
        }
    }
}

@Composable
fun BoostLauncher(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    BoostLauncher(
        title = stringResource(R.string.shine_label),
        description = stringResource(R.string.shine_description),
        prompt = stringResource(R.string.promote_label),
        color = MaterialTheme.colorScheme.onBackground,
        hasWarning = false,
        onCancel = onCancel,
        onConfirm = onConfirm
    ) {
        Image(
            painter = painterResource(R.drawable.ic_booster),
            contentDescription = stringResource(R.string.boost_label),
        )
    }
}

@Composable
fun BoostHiddenContentLauncher(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    BoostLauncher(
        title = stringResource(R.string.hidden_boost_label),
        description = stringResource(R.string.hidden_boost_description),
        prompt = stringResource(R.string.hidden_boost_prompt),
        color = PeerAppDarkRed,
        hasWarning = true,
        onCancel = onCancel,
        onConfirm = onConfirm
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_reports),
            contentDescription = stringResource(R.string.boost_label),
            tint = PeerAppDarkRed,
            modifier = Modifier.clip(CircleShape)
                .background(PeerAppDarkRed.copy(alpha = .1f))
                .padding(6.dp)
        )
    }
}

@Composable
fun BoostReportedContentLauncher(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    BoostLauncher(
        title = stringResource(R.string.reported_boost_label),
        description = stringResource(R.string.reported_boost_description),
        prompt = stringResource(R.string.hidden_boost_prompt),
        color = PeerAppDarkRed,
        hasWarning = true,
        onCancel = onCancel,
        onConfirm = onConfirm
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_reports),
            contentDescription = stringResource(R.string.boost_label),
            tint = PeerAppDarkRed,
            modifier = Modifier.clip(CircleShape)
                .background(PeerAppDarkRed.copy(alpha = .1f))
                .padding(6.dp)
        )
    }
}

@Preview
@Composable
fun PreviewBoostLauncher() {
    DesignTheme(isDarkMode = true) {
        Column {
            BoostLauncher({}) {}
            BoostHiddenContentLauncher({}) {}
            BoostReportedContentLauncher({}) {}
        }
    }
}
