package eu.peernetwork.app.ui.version

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.RawRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun VersionPage(
    version: String,
    versionCode: Int,
    onAppWikiClicked: () -> Unit,
    onBackendWikiClicked: () -> Unit,
) {
    val context = LocalContext.current
    val markdown = remember { context.loadMarkdownFromRaw(R.raw.version) }
    DesignTheme {
        DesignScaffold(
            alwaysReturn = true,
            modifier = Modifier.fillMaxSize(),
            header = {
                val border = MaterialTheme.colorScheme.surfaceContainerLow
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .drawBehind {
                            val y = size.height - 2f
                            drawLine(
                                color = border,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 2f
                            )
                        }.padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DesignOutlineButton(
                        onClick = onAppWikiClicked,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    ) { Text(stringResource(R.string.app_wiki)) }
                    DesignOutlineButton(
                        onClick = onBackendWikiClicked,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    ) { Text(stringResource(R.string.backend_wiki)) }
                }
            },
        ) {
            Column(modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                VersionHeader(
                    version = version,
                    versionCode = versionCode,
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
                )
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.padding(horizontal = 24.dp)
                        .padding(bottom = 48.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    syntaxHighlightColor = MaterialTheme.colorScheme.background,
                    syntaxHighlightTextColor = MaterialTheme.colorScheme.outline,
                    headingBreakColor = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

private fun Context.loadMarkdownFromRaw(@RawRes resId: Int): String {
    return resources.openRawResource(resId)
        .bufferedReader()
        .use { it.readText() }
}

@Composable
private fun VersionHeader(
    version: String,
    versionCode: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth().then(modifier)) {
        Text(
            text = version,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "Version: $version ($versionCode)",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DailyFreebiesPreview() {
    PeerTheme {
        VersionPage(
            version = "1.0.0",
            versionCode = 1,
            onAppWikiClicked = {}
        ) {}
    }
}
