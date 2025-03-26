package eu.peernetwork.user.ui.user.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun UserAvatar(name: String, imageUrl: String) {
    val isAvatarLoaded = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.size(64.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            onSuccess = { isAvatarLoaded.value = true }
        )
        Text(
            text = name[0].toString(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.graphicsLayer {
                if (isAvatarLoaded.value) alpha = 0f else 1f
            }
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserAvatar() {
    PeerTheme {
        UserAvatar(name = "John", imageUrl = "")
    }
}
