package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignAsyncImage(
    label: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    style: TextStyle = MaterialTheme.typography.titleLarge.copy(
        color = MaterialTheme.colorScheme.tertiary,
        fontWeight = FontWeight.Normal
    ),
    onImageLoaded: (Boolean) -> Unit = {}
) {
    val isAvatarLoaded = remember { mutableStateOf(false) }

    Box(modifier = modifier.size(size).background(color)) {
        AsyncImage(
            model = imageUrl,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            onSuccess = {
                isAvatarLoaded.value = true
                onImageLoaded(true)
            },
            onError = {
                isAvatarLoaded.value = false
                onImageLoaded(false)
            }
        )
        Text(
            text = label[0].toString().uppercase(),
            style = style,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                if (isAvatarLoaded.value) alpha = 0f else 1f
            }

        )
    }
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignAsyncImage() {
    PeerTheme {
        DesignAsyncImage(
            label = "John Doe",
            imageUrl = "http://localhost"
        )
    }
}
