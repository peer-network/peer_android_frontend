package eu.peernetwork.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import eu.peernetwork.core.ui.design.luna.DesignAvatar

@Composable
fun HomeSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HeaderSection()
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Spacer(modifier = Modifier.weight(1f)
                    .height(42.dp)
                    .padding(end = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ))
                Spacer(modifier = Modifier.weight(1f)
                    .height(42.dp)
                    .padding(start = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ))
            }
            PostCardSkeleton()
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PlaceholderBar(width = 64.dp, height = 18.dp)
        PlaceholderBar(width = 120.dp, height = 18.dp)
    }
}


@Composable
private fun PostCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DesignAvatar(modifier = Modifier.size(48.dp)) {
                Box(modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow))
            }
            PlaceholderBar(
                widthFraction = 0.4f,
                height = 12.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            )
        }
        Spacer(modifier = Modifier.height(76.dp))
    }
}

@Composable
private fun PlaceholderBar(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    widthFraction: Float? = null,
    height: Dp,
) {
    val baseModifier = modifier
        .clip(RoundedCornerShape(6.dp))
        .background(color)
        .then(
            when {
                width != null -> Modifier.width(width)
                widthFraction != null -> Modifier.fillMaxWidth(widthFraction)
                else -> Modifier.fillMaxWidth()
            }
        )
        .height(height)
    Spacer(modifier = baseModifier)
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewHomeSkeleton() {
    PeerTheme {
        HomeSkeleton()
    }
}
