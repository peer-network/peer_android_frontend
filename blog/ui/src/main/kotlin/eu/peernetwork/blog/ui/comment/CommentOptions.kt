package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerAppRed

@Composable
fun CommentOptions(
    likes: Int,
    isLiked: Boolean,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = !isLiked,
        colors = if (isLiked) {
            IconButtonDefaults.iconButtonColors(
                contentColor = PeerAppRed,
                disabledContentColor = PeerAppRed
            )
        } else {
            IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.surfaceDim,
                disabledContentColor = MaterialTheme.colorScheme.surfaceDim
            )
        }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Icon(
                    painter = painterResource(R.drawable.ic_like),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.padding(bottom = 16.dp))
            }
            Text(
                "$likes",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
