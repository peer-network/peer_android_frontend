package eu.peernetwork.wallet.ui.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiRecipient

@Composable
fun TransferPreview(
    price: String,
    message: String,
    recipient: UiRecipient,
    enabled: State<Boolean>,
    isLoading: State<Boolean>,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onSend: () -> Unit,
    onAuthorClicked: () -> Unit,
    onMessageClicked: (DesignRichText, String) -> Unit,
    rate: @Composable ColumnScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val updatedContent by rememberUpdatedState(content)
    DesignRefreshScaffold(isRefreshing, onRefresh = onRefresh) {
        DesignScaffold(
            alwaysReturn = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            header = {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(bottom = 10.dp)
                ) { updatedContent() }
            }
        ) {
            Column {
                TransferRecipient(
                    recipient = recipient,
                    onClick = onAuthorClicked
                )
                TransferSummary(
                    amount = price,
                    modifier = Modifier.padding(top = 10.dp),
                    content = rate
                )
                if (message.isNotEmpty()) {
                    TransferMessage(
                        message = message,
                        onClick = onMessageClicked,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    DesignOutlineButton(
                        onClick = onBack,
                        minHeight = 48.dp,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.weight(1f),
                    ) { Text(stringResource(R.string.back_label)) }
                    DesignButton(
                        onClick = onSend,
                        minHeight = 48.dp,
                        enabled = !isLoading.value && enabled.value,
                        isLoading = isLoading.value,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.weight(1f),
                    ) { Text(stringResource(R.string.send_label)) }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTransferPreview() {
    DesignTheme(isDarkMode = true) {
        val enabled = remember { mutableStateOf(false) }
        val isLoading = remember { mutableStateOf(false) }
        val recipient = UiRecipient(
            id = "1",
            username = "constantine",
            slug = "1234",
            imageUrl = "http:localhost"
        )
        TransferPreview(
            price = "1 760",
            recipient = recipient,
            message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris vel egestas urna, vitae molestie neque.",
            enabled = enabled,
            isLoading = isLoading,
            onRefresh = {},
            onBack = {},
            onSend = {},
            onAuthorClicked = {},
            onMessageClicked = { _,_ -> }
        ) {}
    }
}
