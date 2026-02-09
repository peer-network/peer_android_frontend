package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.wallet.ui.R

@Composable
fun ConfirmationError(
    error: String,
    onCancel: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .navigationBarsPadding()) {
        ConfirmationTitle(
            size = 16.dp,
            title = stringResource(R.string.error_title),
            painter = painterResource(R.drawable.ic_warning),
        )
        Text(
            text = error,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 20.dp)
                .padding(end = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 20.dp)
                .padding(bottom = 12.dp)
        ) {
            DesignOutlineButton(
                onClick = onCancel,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_label)) }
            DesignButton(
                onClick = onRefresh,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designSecondaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.retry_label)) }
        }
    }
}

@Preview
@Composable
fun PreviewConfirmationError() {
    DesignTheme(isDarkMode = true) {
        ConfirmationError(
            error = "An error occurred while processing your request.",
            onCancel = {},
            onRefresh = {}
        )
    }
}
