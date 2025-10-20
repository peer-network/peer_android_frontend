package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignErrorLabel(
    onRefresh: () -> Unit = {},
    error: Throwable,
    resource: ResourceInteractor,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp),
) {
    val errorMessage = stringResource(R.string.unknown_error_message)
    val noContentMessage = stringResource(R.string.empty_message)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        Box(modifier = Modifier.clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_warning),
                contentDescription = stringResource(R.string.error_label),
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(14.dp)
                    .size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        DesignErrorText(
            if (error is NoContentException) {
                Throwable(noContentMessage, error)
            } else { Throwable(resource.string(error.message ?: errorMessage), error) },
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(onRefresh) {
            Icon(
                painterResource(R.drawable.ic_refresh),
                contentDescription = stringResource(R.string.retry_label),
                tint = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier.padding(12.dp)
                    .size(24.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignErrorLabel() {
    PeerTheme {
        val resource = remember { object : ResourceInteractor {
            override fun getBaseUrl(): String = ""

            override fun string(key: String): String = key
        } }
        DesignErrorLabel(
            error = RuntimeException(),
            resource = resource
        )
    }
}
