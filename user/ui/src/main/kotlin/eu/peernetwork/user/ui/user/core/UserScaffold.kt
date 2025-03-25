package eu.peernetwork.user.ui.user.core

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignAvatar
import eu.peernetwork.core.ui.compose.DesignDetail
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun UserScaffold(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    avatar: @Composable (() -> Unit)? = null,
    indicator: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    options: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    DesignDetail(
        modifier = modifier,
        textStyle = textStyle,
        lead = { avatar?.run {
                DesignAvatar(
                    icon = { indicator?.invoke() },
                    modifier = Modifier.padding(end = 8.dp)
                ) { avatar() }
        } },
        trailing = { Row { actions?.invoke() } },
        options = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp)
            ) { options?.invoke() }
        }) { Box(modifier = Modifier
        .weight(1f)
        .padding(start = 8.dp)) { content() } }
}

@Composable
fun UserSkeleton(modifier: Modifier = Modifier) {
    UserScaffold(
        modifier = modifier,
        avatar = {
            Box(modifier = Modifier.size(64.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer))
        },
        options = {
            Box(modifier = Modifier.fillMaxWidth()
                .height(48.dp)
                .padding(start = 8.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer))
        }
    ) {
        Box(modifier = Modifier.width(100.dp)
            .padding(bottom = 8.dp)
            .height(8.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer))
        Box(modifier = Modifier.width(160.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .height(8.dp))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserScaffold() {
    PeerTheme {
        Column {
            UserSkeleton(modifier = Modifier.padding(16.dp))
            UserScaffold(
                modifier = Modifier.padding(16.dp),
                avatar = {
                    Box(modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                    )
                },
                actions = {
                    Text(text = "icon")
                },
                options = {
                    Text(text = "Overview")
                }
            ) {
                Column {
                    Text(text = "John Doe")
                    Text(
                        text = "Description...",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = .6f
                            )
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
