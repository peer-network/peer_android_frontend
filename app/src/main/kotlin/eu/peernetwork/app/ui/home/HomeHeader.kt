package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignTitleBar
import eu.peernetwork.core.ui.design.compose.DesignTitleBarRegistry
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignTitleBarRegistry.HomeHeader(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    onClick: (HomeRoute) -> Unit = {},
    options: @Composable () -> Unit = {},
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onSurface,
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
            options()
            IconButton(onClick = { onClick(HomeRoute.Comment) }) {
                Icon(
                    painter = painterResource(id = HomeRoute.Comment.icon),
                    contentDescription = stringResource(id = HomeRoute.Comment.icon),
                    modifier = Modifier.size(32.dp)
                )
            }
            Box(modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeHeader() {
    PeerTheme {
        DesignTitleBar {
            HomeHeader {
                attach("tag", {}) {
                    Text("Hello, world!")
                }
            }
        }
    }
}
