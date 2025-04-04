package eu.peernetwork.core.ui.design.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignErrorContent(
    error: Throwable,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    label: (@Composable () -> Unit)? = null,
    content: (@Composable (Throwable) -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        content?.invoke(error) ?: DesignError(error)
        DesignOutlinedButton(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp),
            content = {
                label?.invoke() ?: Text(
                    stringResource(R.string.retry_label),
                    modifier = Modifier.padding(horizontal = 18.dp)
                )
            }
        )
    }
}

@Composable
fun ColumnScope.DesignError(
    error: Throwable,
    color: Color = MaterialTheme.colorScheme.tertiary
) {
    Image(
        painter = painterResource(R.drawable.ic_error),
        contentDescription = stringResource(R.string.error_label),
        colorFilter = ColorFilter.tint(color)
    )
    DesignErrorText(error)
}

@Composable
fun DesignErrorText(
    error: Throwable,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(
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
fun DesignErrorDetail(
    error: Throwable,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    content: (@Composable (Throwable) -> Unit)? = null,
) {
    DesignDetailLayout(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        lead = {
            DesignAvatar(
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_error),
                    contentDescription = stringResource(R.string.error_label),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                )
            } },
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                content?.invoke(error)
                    ?: DesignErrorText(
                        error = error,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
            }
            DesignOutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.bodySmall,
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 24.dp),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(28.dp),
                content = {
                    Text(
                        text = stringResource(R.string.retry_label),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignError() {
    PeerTheme {
        Column {
            DesignErrorDetail(RuntimeException(),
                modifier = Modifier.padding(16.dp))
            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground))
            DesignErrorContent(
                error = RuntimeException(),
                modifier = Modifier.fillMaxWidth().weight(1f),
                onRetry = {  }
            )
            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground))
            DesignErrorContent(
                error = RuntimeException(),
                modifier = Modifier.fillMaxWidth().weight(1f),
                label = { Text("Hello, world!") },
                onRetry = {  }
            )
        }
    }
}
