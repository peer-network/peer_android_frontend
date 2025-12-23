package eu.peernetwork.social.ui.referral

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
import eu.peernetwork.social.ui.R

@Composable
fun ReferralPage(
    onRefresh: () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    DesignRefreshScaffold(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        DesignScaffold(
            alwaysReturn = true,
            modifier = Modifier.fillMaxSize(),
            header = { updatedHeader() },
        ) { state ->
            DesignScaffold(
                modifier = Modifier.fillMaxSize(),
                header = {
                    Text(
                        text = stringResource(R.string.referrals_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                            .padding(horizontal = 18.dp)
                    )
                },
            ) { updatedContent() }
        }
    }
}

@Preview
@Composable
fun PreviewReferralPage() {
    DesignTheme {
        ReferralPage(
            onRefresh = {},
            header = {
                ReferralHeader(remember { mutableStateOf(false) }) {}
            }
        ) {}
    }
}
