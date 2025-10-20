package eu.peernetwork.app.ui.welcome

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.design.material.DesignPage
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun WelcomeScaffold(
    content: @Composable () -> Unit,
) {
    val versionName = BuildConfig.VERSION_NAME
    val updatedContent by rememberUpdatedState(content)
    DesignPage(
        header = { },
        footer = { }
    ) {
        Column(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.weight(1f)) { updatedContent() }
            Text(
                text = stringResource(R.string.version_label, versionName),
                color = MaterialTheme.colorScheme.outlineVariant,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
                    .clickable {

                    },
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewWelcomeScaffold() {
    PeerTheme {
        WelcomeScaffold {

        }
    }
}
