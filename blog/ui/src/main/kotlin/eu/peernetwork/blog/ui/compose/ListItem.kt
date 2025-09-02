package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    lead: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedLead by rememberUpdatedState(lead)
    val updatedContent by rememberUpdatedState(content)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier.clip(CircleShape)) {
            updatedLead()
        }
        Spacer(modifier = Modifier.width(12.dp))
        updatedContent()
    }
}

@Composable
fun ListItem() {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        lead = {
            Box(modifier = Modifier.size(42.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant))
        }
    ) {
        Row {
            Box(modifier = Modifier.weight(.6f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Box(modifier = Modifier.weight(.4f))
        }
    }
}

@Preview
@Composable
fun PreviewListItem() {
    PeerTheme {
        ListItem()
    }
}

