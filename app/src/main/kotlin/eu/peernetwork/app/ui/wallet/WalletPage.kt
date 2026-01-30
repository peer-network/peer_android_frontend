package eu.peernetwork.app.ui.wallet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun WalletPage(
    onClick: () -> Unit,
    onRefresh: () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    val isRefreshing = remember { mutableStateOf(false) }
    DesignRefreshScaffold(isRefreshing, onRefresh = onRefresh) {
        DesignScaffold(
            alwaysReturn = true,
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 18.dp),
            header = {
                Box(
                    modifier = Modifier.padding(top = 8.dp)
                        .padding(bottom = 10.dp)
                ) { updatedHeader() }
            }
        ) {
            DesignScaffold(
                alwaysReturn = true,
                modifier = Modifier.fillMaxSize(),
                header = { WalletTransferButton(onClick = onClick) }
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.transactions_label),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 12.dp)
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 10.dp)
                    )
                    updatedContent()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWalletPage() {
    DesignTheme(isDarkMode = true) {
        WalletPage(
            onClick = {},
            onRefresh = {},
            header = {  },
        ) { }
    }
}
