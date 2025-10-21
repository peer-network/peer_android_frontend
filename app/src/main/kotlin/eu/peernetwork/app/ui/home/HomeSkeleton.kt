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
import eu.peernetwork.core.ui.design.compose.DesignPage
import eu.peernetwork.core.ui.theme.PeerTheme
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement

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
            Column(verticalArrangement = Arrangement.spacedBy(60.dp)) {
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
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 10.dp)
        ,
        horizontalArrangement = Arrangement.spacedBy(100.dp)
    ) {
        PlaceholderBar(width = 80.dp, height = 20.dp)
        PlaceholderBar(width = 120.dp, height = 15.dp)
    }
}


@Composable
private fun PostCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
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
                PlaceholderBar(widthFraction = 0.4f, height = 10.dp)
                PlaceholderBar(widthFraction = 0.6f, height = 10.dp)
            }
            PlaceholderBar(width = 80.dp, height = 25.dp)
        }
        Spacer(modifier = Modifier.padding(vertical = 120.dp))
    }
    Column(
    modifier = Modifier.offset(y=((-55).dp))
    ) {
        PlaceholderBar(height = 20.dp)
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
    widthFraction: Float? = null,
    height: Dp,
) {
    val baseModifier = modifier
        .clip(RoundedCornerShape(6.dp))
        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
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
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
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
