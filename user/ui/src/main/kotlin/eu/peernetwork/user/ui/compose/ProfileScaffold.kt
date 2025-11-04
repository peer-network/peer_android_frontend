package eu.peernetwork.user.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.material.DesignDetail
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ProfileScaffold(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    contentPadding: PaddingValues = PaddingValues(top = 12.dp),
    avatar: @Composable (() -> Unit)? = null,
    indicator: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    options: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    DesignDetail(
        modifier = modifier.padding(contentPadding),
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
                    .padding(top = 6.dp, bottom = 2.dp)
            ) { options?.invoke() }
        }) { Box(modifier = Modifier
        .weight(1f)
        .padding(start = 8.dp)) { content() } }
}

@Composable
fun ProfileScaffold(modifier: Modifier = Modifier) {
    ProfileScaffold(
        modifier = modifier,
        avatar = {
            Box(modifier = Modifier.size(64.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer))
        },
    ) {
        Column {
            Box(modifier = Modifier.width(100.dp)
                .padding(top = 8.dp)
                .height(16.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(4.dp)))
            Box(modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp)
                .height(48.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(4.dp)))
        }
    }
}

@Composable
fun ProfileScaffold(
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit,
) {
    ProfileScaffold(
        modifier = modifier,
        avatar = {
            Box(modifier = Modifier.size(64.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer))
        },
        actions = actions,
    ) {
        Column {
            Box(modifier = Modifier.width(100.dp)
                .padding(top = 8.dp)
                .height(16.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(4.dp)))
            Box(modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp, end = 8.dp)
                .height(48.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(4.dp)))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewProfileScaffold() {
    PeerTheme {
        Column {
            ProfileScaffold(modifier = Modifier.padding(16.dp))
            ProfileScaffold(modifier = Modifier.padding(16.dp)) { Text(text = "icon") }
            ProfileScaffold(
                modifier = Modifier.padding(16.dp),
                avatar = {
                    Box(modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                    )
                },
                actions = { Text(text = "icon") },
                options = {
                    Text(text = "Overview", modifier = Modifier.padding(start = 8.dp))
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
