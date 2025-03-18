package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun HomeHeader(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    options: @Composable RowScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .windowInsetsPadding(WindowInsets.statusBars),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(start = 24.dp))
        CompositionLocalProvider(LocalTextStyle provides textStyle.copy(
            color = MaterialTheme.colorScheme.onBackground
        )) {
            title()
        }
        Spacer(modifier = Modifier.weight(1f))
        Row (
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceTint)
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
                options()
            }
        }
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            actions()
        }
        Box(modifier = Modifier.padding(start = 24.dp))
    }
}

@Composable
fun HomeHeaderOption(
    text: String,
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 8.dp),
    tint: Color = LocalContentColor.current,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
) {
    Row(
        modifier = Modifier.padding(contentPaddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            tint = tint,
            contentDescription = contentDescription,
            modifier = modifier
        )
        CompositionLocalProvider(LocalTextStyle provides textStyle) {
            Text(text)
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeHeader() {
    PeerTheme {
        HomeHeader(
            title = {
                Text("Friends")
            },
            options = {
                HomeHeaderOption(
                    text = "2",
                    painter = painterResource(id = R.drawable.ic_chat_outline),
                    contentDescription = "Action Icon"
                )
                HomeHeaderOption(
                    text = "1",
                    painter = painterResource(id = R.drawable.ic_chat_outline),
                    contentDescription = "Action Icon"
                )
                HomeHeaderOption(
                    text = "3",
                    painter = painterResource(id = R.drawable.ic_chat_outline),
                    contentDescription = "Action Icon"
                )
            }
        ) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chat_outline),
                    contentDescription = "Action Icon"
                )
            }
        }
    }
}
