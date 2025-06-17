package eu.peernetwork.app.ui.setup

import eu.peernetwork.app.BuildConfig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SetupFooter(
    onPrivacy: () -> Unit,
    onAbout: (Int) -> Unit,
) {
    val versionName = BuildConfig.VERSION_NAME
    val version = remember { buildAnnotatedString { append("v$versionName") } }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = onPrivacy,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.privacy_text),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = TextDecoration.Underline,
                )
            )
        }
        ClickableText(
            text = version,
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            onClick = onAbout
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSetupFooter() {
    PeerTheme {
        SetupFooter({}) {}
    }
}
