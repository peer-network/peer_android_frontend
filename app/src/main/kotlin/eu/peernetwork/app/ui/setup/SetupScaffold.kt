package eu.peernetwork.app.ui.setup

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
fun SetupScaffold(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    val updatedFooter by rememberUpdatedState(footer)
    BoxWithConstraints {
        val height = remember { maxHeight * .3f }
        val bottom = (WindowInsets.ime.getBottom(LocalDensity.current) * .2).dp
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            updatedHeader()
            Box(modifier = Modifier.defaultMinSize(
                minHeight = height
            )) { updatedContent() }
            updatedFooter()
            Spacer(Modifier.height(bottom))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupScaffold() {
    PeerTheme {
        SetupScaffold(
            header = {
                Text(
                    text = "Header",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            footer = {
                Text(
                    text = "Footer",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Content",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
