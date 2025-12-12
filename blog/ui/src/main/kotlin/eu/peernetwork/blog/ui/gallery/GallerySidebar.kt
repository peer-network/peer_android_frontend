package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun GallerySidebar(
    engagement: UiEngagement,
    onEngage: (EngagementReaction.State) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp),) {
            EngagementReaction(
                engagement = engagement,
                orientation = Orientation.Vertical,
                onClick = onEngage,
                size = 24.dp
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.width(56.dp)
                .aspectRatio(1f)
        ) {
            IconButton(
                onClick = onMenu,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_option),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewGallerySidebar() {
    val engagement = UiEngagement(
        id = "<test-id>",
        likes = "5k",
        dislikes = "1k",
        isDisliked = false,
        isLiked = false,
        views = "3k",
        comment = "1k"
    )
    DesignTheme(isDarkMode = true) {
        GallerySidebar(
            engagement = engagement,
            onEngage = {},
            onMenu = {},
            Modifier.width(56.dp)
        )
    }
}
