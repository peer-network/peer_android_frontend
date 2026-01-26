package eu.peernetwork.wallet.ui.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignErrorText
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.material.DesignCard
import eu.peernetwork.core.ui.design.material.DesignDetailLayout
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DashboardError(
    error: Throwable,
    resource: ResourceInteractor,
    onRetry: () -> Unit
) {
    val errorMessage = stringResource(R.string.unknown_error_message)
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        DesignDetailLayout(
            lead = {
                DesignAvatar {
                    Box(modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_warning),
                            contentDescription = stringResource(R.string.error_label),
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                } },
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 12.dp)
            ) {
                DesignErrorText(
                    Throwable(resource.string(error.message ?: errorMessage), error),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onRetry) {
                    Icon(
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.surfaceDim
                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewErrorLabel() {
    PeerTheme {
        val resource = remember { object : ResourceInteractor {
            override fun getBaseUrl(): String = ""
            override fun string(key: String): String = key
        } }
        DashboardError(RuntimeException(), resource) {}
    }
}
