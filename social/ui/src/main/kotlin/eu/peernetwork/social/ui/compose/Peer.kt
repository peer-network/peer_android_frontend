package eu.peernetwork.social.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.model.UiMember
import java.util.UUID

@Composable
fun Peer(
    member: UiMember,
    onClick: (UiMember) -> Unit,
    action: (@Composable () -> Unit)? = null
) {
    val slug = "#${member.slug}"
    val handleOnClick by rememberUpdatedState(onClick)
    SearchItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { handleOnClick(member) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        lead = {
            DesignAsyncImage(
                label = member.username,
                imageUrl = member.imageUrl,
                size = 42.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) {
        Row {
            Text(
                text = "@${member.username} $slug".annotate(
                    slug,
                    style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            action?.invoke()
        }
    }
}

@Preview
@Composable
fun PreviewFollowerItem() {
    PeerTheme {
        val model = UiMember(
            id = UUID.randomUUID().toString(),
            slug = "1234",
            username = "johnDoe",
            imageUrl = "http://localhost"
        )
        Peer(model, onClick = {}) {
            ConnectionScreen(
                isFollowing = true,
                isFollowed = true,
                onClick = {}
            )
        }
    }
}