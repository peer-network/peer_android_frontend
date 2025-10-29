package eu.peernetwork.social.ui.search.member

import androidx.compose.foundation.clickable
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
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.compose.SearchItem
import eu.peernetwork.social.ui.model.UiMember
import java.util.UUID

@Composable
fun MemberItem(
    model: UiMember,
    onClick: (String) -> Unit = {}
) {
    val slug = "#${model.slug}"
    val handleOnClick by rememberUpdatedState(onClick)
    SearchItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { handleOnClick(model.id) }
            .padding(vertical = 8.dp),
        lead = {
            DesignImage(
                label = model.username,
                imageUrl = model.imageUrl,
                size = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) {
        Text(
            text = "@${model.username} $slug".annotate(
                slug,
                style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
fun PreviewMemberItem() {
    PeerTheme {
        val model = UiMember(
            id = UUID.randomUUID().toString(),
            slug = "1234",
            username = "johnDoe",
            imageUrl = "http://localhost",
            isFollowing = false,
            isFollowed = false
        )
        MemberItem(model) {}
    }
}
