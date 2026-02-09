package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.blog.ui.R

@Composable
fun CommentToolbar(
    username: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        DesignAvatar {
            DesignImage(
                label = username,
                imageUrl = imageUrl,
                size = 42.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
        DesignSkeleton(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.weight(1f)
                .padding(start = 10.dp)
                .height(48.dp)
                .clickable(onClick = onClick)
        ) {
            Text(
                text = stringResource(R.string.post_reply),
                color = MaterialTheme.colorScheme.scrim,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterStart)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
@Preview
fun PreviewPostHeader() {
    DesignTheme(isDarkMode = true) {
        CommentToolbar(
            username = "John Doe",
            imageUrl = "http://localhost",
        ) {}
    }
}
