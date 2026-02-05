package eu.peernetwork.app.ui.wallet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun WalletPage(
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
        ) { updatedContent() }
    }
}

@Preview
@Composable
fun PreviewWalletPage() {
    DesignTheme(isDarkMode = true) {
        WalletPage(
            onRefresh = {},
            header = {  },
        ) { }
    }
}
