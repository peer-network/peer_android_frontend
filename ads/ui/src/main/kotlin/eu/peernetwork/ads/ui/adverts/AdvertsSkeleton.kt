package eu.peernetwork.ads.ui.adverts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun AdvertsSkeleton(modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxWidth()
        .then(modifier)
        .clip(RoundedCornerShape(24.dp))
        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        .padding(10.dp)) {
        Box(
            modifier = Modifier.height(86.dp)
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceDim)
        )
    }
}

@Preview
@Composable
fun PreviewAdvertsSkeleton() {
    DesignTheme(isDarkMode = true) {
        AdvertsSkeleton()
    }
}
