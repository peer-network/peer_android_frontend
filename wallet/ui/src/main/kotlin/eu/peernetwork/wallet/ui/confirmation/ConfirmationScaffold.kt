package eu.peernetwork.wallet.ui.confirmation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ConfirmationScaffold(
    title: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedTitle by rememberUpdatedState(title)
    val updatedFooter by rememberUpdatedState(footer)
    val updatedContent by rememberUpdatedState(content)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onPrimary
        )) { updatedTitle() }
        Spacer(modifier = Modifier.height(12.dp))
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.tertiary,
            LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            )
        ) { updatedContent() }
        Spacer(modifier = Modifier.height(18.dp))
        updatedFooter()
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ConfirmationScaffold() {
    ConfirmationScaffold({
        Box(modifier = Modifier.width(64.dp)
            .height(16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp)))
    }, {
        Box(modifier = Modifier.fillMaxWidth()
            .height(42.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)))
    }) {
        Box(modifier = Modifier.width(96.dp)
            .height(16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp)))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewConfirmationScaffold() {
    PeerTheme {
        ConfirmationScaffold()
    }
}
