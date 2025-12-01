package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

enum class DesignPageWindowMode {
    DOCKED,
    FLOATING,
    HIDDEN
}

@Composable
fun DesignPage(
    mode: DesignPageWindowMode = DesignPageWindowMode.HIDDEN,
    header: @Composable DesignTitleBarRegistry.(State<Float>) -> Unit = {},
    footer: @Composable DesignTitleBarRegistry.(State<Float>) -> Unit = {},
    content: @Composable (State<Float>) -> Unit,
) {
    val state = remember { mutableFloatStateOf(1f) }
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    if (mode == DesignPageWindowMode.DOCKED) {
        DesignPage(header, footer, content)
    } else if (mode == DesignPageWindowMode.FLOATING) {
        DesignTitleBar {
            Box {
                updatedContent(state)
                updatedHeader(state)
            }
        }
    } else if (mode == DesignPageWindowMode.HIDDEN) {
        updatedContent(state)
    }
}

@Composable
fun DesignPage(
    header: @Composable DesignTitleBarRegistry.(State<Float>) -> Unit,
    footer: @Composable DesignTitleBarRegistry.(State<Float>) -> Unit,
    content: @Composable (State<Float>) -> Unit,
) {
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    val updatedFooter by rememberUpdatedState(footer)
    DesignTitleBar {
        DesignScaffold(
            alwaysReturn = true,
            header = { updatedHeader(it) },
            footer = { updatedFooter(it) },
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        ) { state -> updatedContent(state) }
    }
}

@Composable
fun DesignTitleBarRegistry.DesignPageHeader(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    action: @Composable () -> Unit = {},
    options: @Composable () -> Unit = {},
) {
    val updatedAction by rememberUpdatedState(action)
    val updatedOption by rememberUpdatedState(options)
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onBackground,
        LocalTextStyle provides textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = modifier
                .wrapContentHeight()
                .windowInsetsPadding(WindowInsets.statusBars),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(start = 16.dp))
            Box(modifier = Modifier.weight(1f)) { titleBar().value?.content?.invoke() }
            Box(modifier = Modifier.padding(start = 16.dp))
            updatedOption()
            updatedAction()
            Box(modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignPage() {
    PeerTheme {
        DesignPage(
            header = {
                DesignPageHeader(
                    action = {
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chat),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                ) },
            footer = { }
        ) { state ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Hello, world!",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
