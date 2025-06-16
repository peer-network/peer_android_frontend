package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignLabel
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

@Composable
fun TransferSheetScaffold(
    state: State<Boolean>,
    error: State<String?>,
    title: String,
    recipient: UiRecipient,
    token: BigDecimal,
    action: String,
    onClick: () -> Unit = {},
    onSubmit: () -> Unit,
    icon: @Composable () -> Unit
) {
    var errorMessage by remember { mutableStateOf(error.value) }
    Column(
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            )
            .navigationBarsPadding()
    ) {
        Text(
            stringResource(R.string.amount_text),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            "${token.setScale(4, RoundingMode.HALF_UP)}",
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DesignLabel(
            visible = error.value != null,
            onAnimationEnd = {
                if (it) {
                    errorMessage = error.value
                }
            },
            label = {
                errorMessage?.run {
                    Text(
                        text = this,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                    )
                }
            }
        ) { TransferRecipient(recipient, onClick, icon) }
        Spacer(modifier = Modifier.height(14.dp))
        DesignButton(
            onClick = onSubmit,
            isLoading = state.value,
            enabled = !state.value,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = action,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferSheetScaffold() {
    PeerTheme {
        val recipient = UiRecipient(
            id = UUID.randomUUID().toString(),
            slug = "1234",
            username = "johnDoe",
            imageUrl = "http://localhost"
        )
        TransferSheetScaffold(
            state = remember { mutableStateOf(true) },
            error = remember { mutableStateOf("Hello, world!") },
            title = stringResource(R.string.recipient_label),
            recipient = recipient,
            action = stringResource(R.string.close_label),
            token = BigDecimal(1.0),
            onClick = {},
            onSubmit = {},
        ) { }
    }
}
