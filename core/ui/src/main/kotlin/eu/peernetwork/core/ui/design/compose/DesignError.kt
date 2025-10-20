package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.material.DesignOutlinedButton
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignError(
    error: Throwable,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 24.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    label: (@Composable () -> Unit)? = null,
    content: (@Composable (Throwable) -> Unit)? = null,
) {
    val updatedLabel by rememberUpdatedState(label)
    val updatedContent by rememberUpdatedState(content)
    val noContentMessage = stringResource(R.string.empty_message)
    Column(
        modifier = modifier.padding(contentPaddingValues),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        updatedContent?.invoke(if (error is NoContentException) {
            Throwable(noContentMessage, error)
        } else { error }) ?: DesignError(if (error is NoContentException) {
            Throwable(noContentMessage, error)
        } else { error })
        DesignOutlinedButton(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp),
            content = {
                updatedLabel?.invoke() ?: Text(
                    stringResource(R.string.retry_label),
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }
        )
    }
}

@Composable
fun DesignError(
    error: Throwable,
    color: Color = MaterialTheme.colorScheme.tertiary
) {
    Image(
        painter = painterResource(R.drawable.ic_error),
        contentDescription = stringResource(R.string.error_label),
        colorFilter = ColorFilter.tint(color)
    )
    Spacer(modifier = Modifier.height(8.dp))
    DesignErrorText(error)
}

@Composable
fun DesignErrorText(
    error: Throwable,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = TextAlign.Center,
    style: TextStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.tertiary
    )
) {
    Text(
        if (!error.message.isNullOrEmpty()) {
            error.message!!
        } else {
            stringResource(R.string.unknown_error_message)
        },
        modifier = modifier,
        textAlign = textAlign,
        style = style
    )
}

@Composable
fun DesignError(
    onRefresh: () -> Unit = {},
    error: Throwable,
    resource: ResourceInteractor
) {
    val errorMessage = stringResource(R.string.unknown_error_message)
    val noContentMessage = stringResource(R.string.empty_message)
    DesignError(
        error = if (error is NoContentException) {
            Throwable(noContentMessage, error)
        } else {
            Throwable(
                message = resource.string(error.message ?: errorMessage),
                cause = error
            )
        },
        onRetry = onRefresh,
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignError() {
    PeerTheme {
        Column {
            DesignError(
                error = RuntimeException(),
                modifier = Modifier.fillMaxWidth().weight(1f),
                onRetry = {  }
            )
            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground))
            DesignError(
                error = NoContentException(),
                modifier = Modifier.fillMaxWidth().weight(1f),
                label = { Text("Hello, world!") },
                onRetry = {  }
            )
        }
    }
}
