package eu.peernetwork.wallet.ui.confirmation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.component.DesignErrorText
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ConfirmationError(
    error: Throwable,
    resource: ResourceInteractor,
    onRetry: () -> Unit
) {
    val errorMessage = stringResource(R.string.unknown_error_message)
    val noContentMessage = stringResource(R.string.empty_message)
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    ConfirmationScaffold({
        Box(modifier = Modifier.width(36.dp)
            .height(12.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)))
    }, {
        DesignOutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(28),
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = onPrimary,
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = onPrimary,
                disabledContainerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, onPrimary),
        ) { Text(stringResource(R.string.retry_label)) }
    }) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_warning),
                contentDescription = stringResource(R.string.error_label),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(8.dp))
            DesignErrorText(
                if (error is NoContentException) {
                    Throwable(noContentMessage, error)
                } else { Throwable(resource.string(error.message ?: errorMessage), error) },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            )
            Spacer(modifier = Modifier.weight(.3f))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewConfirmationError() {
    PeerTheme {
        val resource = remember { object : ResourceInteractor {
            override fun getBaseUrl(): String = ""
            override fun string(key: String): String = key
        } }
        ConfirmationError(RuntimeException(), resource) {}
    }
}
