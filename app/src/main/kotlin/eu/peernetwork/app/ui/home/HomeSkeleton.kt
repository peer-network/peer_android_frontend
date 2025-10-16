package eu.peernetwork.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import eu.peernetwork.core.ui.design.material.DesignPage

@Composable
fun HomeSkeleton() {
    DesignPage {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                HeaderSection()
                PostCardSkeleton()
            }
            FooterSection()
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
        PlaceholderBar(width = 64.dp, height = 16.dp)
        PlaceholderBar(width = 120.dp, height = 16.dp)
    }
}


@Composable
private fun PostCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CirclePlaceholder(size = 40.dp)
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                PlaceholderBar(
                    widthFraction = 0.4f,
                    height = 6.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
                PlaceholderBar(
                    widthFraction = 0.6f,
                    height = 6.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
            }
            PlaceholderBar(
                width = 12.dp,
                height = 6.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            )
        }
        Spacer(modifier = Modifier.height(76.dp))
    }
}

@Composable
private fun FooterSection() {
    PlaceholderBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .windowInsetsPadding(WindowInsets.navigationBars),
        height = 25.dp
    )
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
        .clip(RoundedCornerShape(4.dp))
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

@Composable
private fun CirclePlaceholder(size: Dp) {
    Spacer(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    )
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
