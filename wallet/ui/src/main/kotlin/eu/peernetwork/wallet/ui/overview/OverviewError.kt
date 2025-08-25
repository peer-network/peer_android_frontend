package eu.peernetwork.wallet.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.component.DesignErrorText
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun OverviewError(
    error: Throwable,
    resource: ResourceInteractor,
    modifier: Modifier = Modifier
) {
    val errorMessage = stringResource(R.string.unknown_error_message)
    val noContentMessage = stringResource(R.string.empty_message)
    Box(modifier = modifier) {
        OverviewScaffold(
            rate = {
                Box(modifier = Modifier.width(32.dp)
                    .height(12.dp)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp)))
            },
            token = {
                Box(modifier = Modifier.width(120.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp)))
            }
        ) {
            DesignErrorText(
                if (error is NoContentException) {
                    Throwable(noContentMessage, error)
                } else { Throwable(resource.string(error.message ?: errorMessage), error) },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewOverviewError() {
    PeerTheme {
        val resource = remember { object : ResourceInteractor {
            override fun getBaseUrl(): String = ""
            override fun string(key: String): String = key
        } }
        OverviewError(RuntimeException(), resource)
    }
}
